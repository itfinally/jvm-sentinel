package io.github.itfinally.jvm.components;

import com.google.common.hash.Hashing;
import io.github.itfinally.jvm.JvmMonitorProperties;
import io.github.itfinally.jvm.components.events.MonitorEventManager;
import io.github.itfinally.jvm.components.events.MonitorStartingEvent;
import io.github.itfinally.jvm.entity.JvmArgumentsEntity;
import io.github.itfinally.jvm.entity.JvmStatusEntity;
import io.github.itfinally.jvm.vo.JvmRegisterVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.isNullOrEmpty;

public abstract class AbstractMachineIdInitializer implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {
  private final Logger logger = LoggerFactory.getLogger( getClass() );

  private static long machineId;

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

  protected abstract void dataDeliver( JvmRegisterVo jvmRegisterVo, Promise promise );

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

  public static long machineId() {
    return machineId;
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
        .setPort( port )
        .setAddress( address )
        .setName( runtimeMXBean.getVmName() )
        .setVersion( runtimeMXBean.getVmVersion() )
        .setOsName( operatingSystemMXBean.getName() )
        .setJvmHashId( buildMachineId( address, port ) )
        .setJavaVersion( runtimeMXBean.getSpecVersion() )
        .setOsVersion( operatingSystemMXBean.getVersion() )
        .setApplicationName( properties.getApplicationName() )
        .setCompiler( ManagementFactory.getCompilationMXBean().getName() );

    List<JvmArgumentsEntity> argumentsEntities = runtimeMXBean.getInputArguments()
        .stream()

        .map( arg -> new JvmArgumentsEntity()
            .setJvmId( jvmStatusEntity.getId() )
            .setValue( arg ) )

        .collect( Collectors.toList() );

    dataDeliver( new JvmRegisterVo( jvmStatusEntity, argumentsEntities ), new Promise() );
  }

  private long buildMachineId( String address, String port ) {
    String key = String.format( "%s:%s", address, port );
    return Hashing.sha256().hashString( key, Charset.forName( "utf-8" ) ).asLong();
  }

  private void scheduleRetryTask() {

    logger.info( "Machine id initializing will be retry after {} seconds...", retryTimeGap );

    retryWorker.schedule( () -> initializingMachineId( embeddedServletContainerInitializedEvent ),
        retryTimeGap, TimeUnit.SECONDS );

    retryTimeGap *= 2;
  }

  protected class Promise {
    void resolve( long machineId ) {
      properties.setTurnOn( true );

      AbstractMachineIdInitializer.machineId = machineId;

      monitorEventManager.publish( new MonitorStartingEvent( monitorEventManager, properties ) );
    }

    void retry() {
      properties.setTurnOn( false );

      AbstractMachineIdInitializer.this.scheduleRetryTask();
    }
  }
}
