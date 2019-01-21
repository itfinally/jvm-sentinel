package io.github.itfinally.jvm.components;

import io.github.itfinally.jvm.requests.VManagerClient;
import io.github.itfinally.jvm.vo.JvmRegisterVo;
import io.github.itfinally.vo.SingleResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Resource;

import static io.github.itfinally.http.HttpCode.OK;

@Component
@ConditionalOnProperty( prefix = "monitor.java", value = "deliver-on-local", havingValue = "false" )
public class DefaultMachineIdInitializer extends AbstractMachineIdInitializer {
  private static final Logger logger = LoggerFactory.getLogger( DefaultMachineIdInitializer.class );

  @Resource
  private VManagerClient managerClient;

  @Override
  protected void dataDeliver( JvmRegisterVo jvmRegisterVo, Promise promise ) {
    Call<SingleResponseVo<Boolean>> calling = managerClient.vmRegister( jvmRegisterVo );

    calling.enqueue( new Callback<SingleResponseVo<Boolean>>() {
      @Override
      @ParametersAreNonnullByDefault
      public void onResponse( Call<SingleResponseVo<Boolean>> call, Response<SingleResponseVo<Boolean>> response ) {
        if ( !response.isSuccessful() ) {
          logger.warn( "Failure to communicate with host({}), auto-retry later...", calling.request().url().toString() );

          logger.warn( "The server responded with code: {}, message: {}", response.code(), response.message() );

          promise.retry();

          return;
        }

        if ( null == response.body() ) {
          throw new IllegalStateException( "The main service should be return something but got nothing when call 'response.body()'." );
        }

        SingleResponseVo<Boolean> responseBody = response.body();
        if ( OK.getCode() != responseBody.getStatus() ) {
          logger.warn( "Failure to request with host({}), auto-retry later...", calling.request().url().toString() );

          logger.warn( "The server responded with code: {}, message: {}", responseBody.getStatus(), responseBody.getMessage() );

          promise.retry();

          return;
        }

        if ( null == responseBody.getResult() ) {
          throw new IllegalStateException( "The main server should be return something but got nothing at 'responseBody.getResult()'." );
        }

        if ( !responseBody.getResult() ) {
          logger.warn( "" );

          promise.retry();

          return;
        }

        promise.resolve( jvmRegisterVo.getJvmStatusEntity().getId() );
      }

      @Override
      @ParametersAreNonnullByDefault
      public void onFailure( Call<SingleResponseVo<Boolean>> call, Throwable throwable ) {
        logger.warn( "Failure to send request to host({}), auto-retry later...", calling.request().url().toString() );

        logger.error( "Cause by exception: {}, message: {}", throwable.getClass().getSimpleName(), throwable.getMessage() );

        promise.retry();

        call.cancel();
      }
    } );
  }
}