package io.github.itfinally.jvm.components;

import com.google.common.hash.Hashing;
import io.github.itfinally.jvm.JvmMonitorProperties;
import io.github.itfinally.jvm.components.events.MonitorEventManager;
import io.github.itfinally.jvm.components.events.MonitorStartingEvent;
import io.github.itfinally.jvm.entity.JvmArgumentsEntity;
import io.github.itfinally.jvm.entity.JvmStatusEntity;
import io.github.itfinally.jvm.requests.VManagerClient;
import io.github.itfinally.jvm.vo.JvmRegisterVo;
import io.github.itfinally.vo.SingleResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.isNullOrEmpty;
import static io.github.itfinally.http.HttpCode.OK;

@Component
public class MachineIdInitializer implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
  private static final Logger logger = LoggerFactory.getLogger( MachineIdInitializer.class );

  public static long MACHINE_ID = -1;

  @Resource
  private VManagerClient vManagerClient;

  @Resource
  private JvmMonitorProperties properties;

  @Resource
  private MonitorEventManager monitorEventManager;

  @Resource
  @Qualifier( "taskRetryScheduleWorker" )
  private volatile ScheduledExecutorService retryWorker;

  private volatile EmbeddedServletContainerInitializedEvent embeddedServletContainerInitializedEvent;

  private volatile boolean isCallOnce = false;

  private int retryTimeGap = 1;

  @Override
  public void onApplicationEvent( EmbeddedServletContainerInitializedEvent embeddedServletContainerInitializedEvent ) {
    if ( !isCallOnce ) {
      this.embeddedServletContainerInitializedEvent = embeddedServletContainerInitializedEvent;

      isCallOnce = true;

      if ( !properties.isTurnOn() ) {
        return;
      }

      initializingMachineId( embeddedServletContainerInitializedEvent );
    }
  }

  private void initializingMachineId( EmbeddedServletContainerInitializedEvent embeddedServletContainerInitializedEvent ) {
    String address, port;

    try {
      port = Integer.toString( embeddedServletContainerInitializedEvent.getEmbeddedServletContainer().getPort() );
      address = InetAddress.getLocalHost().getHostAddress();

      if ( isNullOrEmpty( port ) ) {
        logger.error( "Can not get port from spring environment" );

        scheduleRetryTask();

        return;
      }

    } catch ( UnknownHostException e ) {
      logger.warn( "Failure to initialize jvm monitor, monitor will be shutdown." );

      logger.error( "Cause by exception: {}, message: {}", e.getClass().getSimpleName(), e.getMessage() );

      properties.setTurnOn( false );

      return;
    }

    RuntimeMXBean runtimeMXBean = ManagementFactory.getRuntimeMXBean();
    OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();

    // Register jvm starting info
    JvmStatusEntity jvmStatusEntity = new JvmStatusEntity()
        .setName( runtimeMXBean.getVmName() )
        .setVersion( runtimeMXBean.getVmVersion() )
        .setOsName( operatingSystemMXBean.getName() )
        .setJvmHashId( buildMachineId( address, port ) )
        .setJavaVersion( runtimeMXBean.getSpecVersion() )
        .setOsVersion( operatingSystemMXBean.getVersion() )
        .setCompiler( ManagementFactory.getCompilationMXBean().getName() );

    List<JvmArgumentsEntity> argumentsEntities = runtimeMXBean.getInputArguments()
        .stream()

        .map( arg -> new JvmArgumentsEntity()
            .setJvmId( jvmStatusEntity.getId() )
            .setValue( arg ) )

        .collect( Collectors.toList() );

    Call<SingleResponseVo<Long>> calling = vManagerClient.vmRegister( new JvmRegisterVo( jvmStatusEntity, argumentsEntities ) );

    calling.enqueue( new Callback<SingleResponseVo<Long>>() {
      @Override
      @ParametersAreNonnullByDefault
      public void onResponse( Call<SingleResponseVo<Long>> call, Response<SingleResponseVo<Long>> response ) {
        if ( !response.isSuccessful() ) {
          logger.warn( "Failure to communicate with host({}), auto-retry later...", calling.request().url().toString() );

          logger.warn( "The server responded with code: {}, message: {}", response.code(), response.message() );

          properties.setTurnOn( false );

          scheduleRetryTask();

          return;
        }

        if ( null == response.body() ) {
          throw new IllegalStateException( "The main service should be return something but got nothing when call 'response.body()'." );
        }

        SingleResponseVo<Long> responseBody = response.body();
        if ( OK.getCode() != responseBody.getStatus() ) {
          logger.warn( "Failure to request with host({}), auto-retry later...", calling.request().url().toString() );

          logger.warn( "The server responded with code: {}, message: {}", responseBody.getStatus(), responseBody.getMessage() );

          properties.setTurnOn( false );

          scheduleRetryTask();

          return;
        }

        if ( null == responseBody.getResult() ) {
          throw new IllegalStateException( "The main server should be return something but got nothing at 'responseBody.getResult()'." );
        }

        properties.setTurnOn( true );

        MACHINE_ID = responseBody.getResult();

        monitorEventManager.publish( new MonitorStartingEvent( monitorEventManager, properties ) );
      }

      @Override
      @ParametersAreNonnullByDefault
      public void onFailure( Call<SingleResponseVo<Long>> call, Throwable throwable ) {
        logger.warn( "Failure to send request to host({}), auto-retry later...", calling.request().url().toString() );

        logger.error( "Cause by exception: {}, message: {}", throwable.getClass().getSimpleName(), throwable.getMessage() );

        properties.setTurnOn( false );

        scheduleRetryTask();

        call.cancel();
      }
    } );
  }

  private long buildMachineId( String address, String port ) {
    String key = String.format( "%s:%s", address, port );
    return Hashing.sha256().hashString( key, Charset.forName( "utf-8" ) ).asLong();
  }

  private void scheduleRetryTask() {
    retryWorker.schedule( () -> initializingMachineId( embeddedServletContainerInitializedEvent ),
        retryTimeGap, TimeUnit.SECONDS );

    retryTimeGap *= 2;
  }
}