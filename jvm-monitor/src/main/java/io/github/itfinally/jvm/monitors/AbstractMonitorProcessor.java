package io.github.itfinally.jvm.monitors;

import io.github.itfinally.vo.BasicResponseVo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Resource;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static io.github.itfinally.http.HttpCode.OK;

public abstract class AbstractMonitorProcessor {
  private final Logger logger = LoggerFactory.getLogger( getClass() );

  @Resource
  @Qualifier( "taskRetryScheduleWorker" )
  private volatile ScheduledExecutorService retryWorker;

  protected void sendData( Call<BasicResponseVo.Default> calling ) {
    calling.enqueue( new Callback<BasicResponseVo.Default>() {

      private final AtomicInteger counter = new AtomicInteger( 0 );

      private final AtomicInteger timeGap = new AtomicInteger( 4 );

      @Override
      @ParametersAreNonnullByDefault
      public void onResponse( Call<BasicResponseVo.Default> call, Response<BasicResponseVo.Default> response ) {
        if ( !response.isSuccessful() ) {
          onFailure( call.clone(), new Throwable( "Request failure with some network issue" ) );

          return;
        }

        BasicResponseVo.Default responseBody = response.body();
        if ( null == responseBody ) {
          logger.warn( "The main server return nothing, unknown processing status" );

          return;
        }

        if ( responseBody.getStatus() != OK.getCode() ) {
          logger.warn( "Request failure, server responding with code: {}, message: {}",
              responseBody.getStatus(), responseBody.getMessage() );

          onFailure( call.clone(), new Throwable( "Request successful, but processing failure with some main server issue" ) );
        }
      }

      @Override
      @ParametersAreNonnullByDefault
      public void onFailure( Call<BasicResponseVo.Default> call, Throwable t ) {

        logger.error( "Failure to send thread information to main server", t );

        if ( counter.getAndIncrement() > 3 ) {

          if ( !call.isCanceled() ) {
            call.cancel();
          }

          logger.error( "The maximum number of request has been exceeded, cancel request" );
          return;
        }

        int lastInterval = timeGap.get();
        int randomInterval = ThreadLocalRandom.current().nextInt( lastInterval, getTimeInterval( timeGap ) );

        if ( !call.isCanceled() ) {
          logger.info( "Class {} try to resend data after {} seconds", getClass().getSimpleName(), randomInterval );

          retryWorker.schedule( () -> call.enqueue( this ), randomInterval, TimeUnit.SECONDS );
        }
      }
    } );
  }

  private int getTimeInterval( AtomicInteger timeGap ) {
    int interval;

    while ( true ) {
      interval = timeGap.get();

      if ( timeGap.compareAndSet( interval, interval * 2 ) ) {
        interval *= 2;
        return interval;
      }
    }
  }
}
