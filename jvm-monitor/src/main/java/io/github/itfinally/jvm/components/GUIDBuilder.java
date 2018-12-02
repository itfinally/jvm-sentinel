package io.github.itfinally.jvm.components;

import io.github.itfinally.jvm.JvmMonitorProperties;
import io.github.itfinally.jvm.exception.IllegalWorkerCodeException;
import org.springframework.stereotype.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;
import java.util.concurrent.*;

import static java.lang.System.currentTimeMillis;

@Component
public class GUIDBuilder {
  private static final int WORKER_CODE = 1024;
  private static final int INCREASE = 4096;
  private static final int RANGE = 32;

  private static final ScheduledExecutorService sentinel = Executors
      .newScheduledThreadPool( 1, new ThreadFactory() {
        @Override
        @ParametersAreNonnullByDefault
        public Thread newThread( Runnable runnable ) {
          Thread thread = new Thread( runnable );
          thread.setName( "unique-id-generator-sentinel" );
          return thread;
        }
      } );

  private final short workerCode;
  private final BlockingQueue<List<Long>> pipeline = new ArrayBlockingQueue<>( INCREASE * 6 );
  private ThreadLocal<Deque<Long>> localPipeline = new ThreadLocal<>();

  public GUIDBuilder( JvmMonitorProperties properties ) {
    WorkerCodeProvider workerCodeProvider;
    try {
      workerCodeProvider = properties.getWorkerCodeProvider().newInstance();

    } catch ( InstantiationException | IllegalAccessException e ) {
      throw new RuntimeException( "Can not new an instance of worker code provider", e );
    }

    short workerCode = workerCodeProvider.getWorkerCode();
    if ( workerCode >= WORKER_CODE ) {
      throw new IllegalWorkerCodeException( "Worker Code should be greater than 0 and less than 1024" );
    }

    if ( workerCode < 0 ) {
      throw new IllegalWorkerCodeException( "Worker Code should be greater than 0 and less than 1024" );
    }

    this.workerCode = workerCode;
    submitSentinelTask();
  }

  public long getId() {
    Deque<Long> deque = localPipeline.get();
    if ( null == deque || deque.isEmpty() ) {
      try {
        deque = new ArrayDeque<>( pipeline.take() );
        localPipeline.set( deque );

      } catch ( InterruptedException e ) {
        throw new RuntimeException( e );
      }
    }

    return deque.pop();
  }

  private void submitSentinelTask() {
    sentinel.scheduleWithFixedDelay( new Runnable() {
      @Override
      public void run() {
        List<Long> batch;
        long currentMillis = currentTimeMillis();

        if ( pipeline.remainingCapacity() <= 0 ) {
          return;
        }

        for ( int index = 0; index < INCREASE; index += RANGE ) {
          batch = new ArrayList<>();

          for ( int innerIndex = index, length = innerIndex + RANGE; innerIndex < length; innerIndex += 1 ) {
            batch.add( currentMillis << 22 | workerCode << 12 | innerIndex );
          }

          // full pipeline
          if ( !pipeline.offer( batch ) ) {
            return;
          }
        }
      }
    }, 0, 1, TimeUnit.MILLISECONDS );
  }
}
