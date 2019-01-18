package io.github.itfinally.jvm.components.events;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.sun.management.GarbageCollectionNotificationInfo;
import io.github.itfinally.jvm.JvmMonitorProperties;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class MonitorEventManager {
  private static final Logger logger = LoggerFactory.getLogger( MonitorEventManager.class );

  private final EventBus eventBus = new AsyncEventBus( new ThreadPoolExecutor( 1, 2,
      1, TimeUnit.SECONDS, new LinkedBlockingDeque<>( 1024 ), new ThreadFactory() {

    private final AtomicInteger counter = new AtomicInteger( 0 );

    @Override
    public Thread newThread( @NotNull Runnable r ) {
      Thread thread = new Thread( r );
      thread.setName( String.format( "monitor-event-publisher-%d", counter.getAndIncrement() ) );
      thread.setUncaughtExceptionHandler( ( t, e ) -> logger.error( "Catch exception in event bus", e ) );

      return thread;
    }

  } ) );

  private ScheduledExecutorService scheduleWorker;

  {
    eventBus.register( this );
  }

  public void register( Object handler ) {
    eventBus.register( handler );
  }

  public void unRegister( Object handler ) {
    eventBus.unregister( handler );
  }

  public void publish( Object event ) {
    eventBus.post( event );
  }

  @Subscribe
  private void monitorActive( MonitorStartingEvent event ) {
    if ( scheduleWorker != null ) {
      return;
    }

    scheduleWorker = Executors.newScheduledThreadPool( 1, r -> {
      Thread thread = new Thread( r );
      thread.setName( "monitor-event-schedule-worker" );
      thread.setUncaughtExceptionHandler( ( t, e ) -> logger.error( "Catch exception in schedule worker", e ) );

      return thread;
    } );

    final JvmMonitorProperties properties = event.getProperties();

    if ( properties.isAllowThreadDetected() ) {
      scheduleWorker.schedule( () -> {
            if ( properties.isTurnOn() ) {
              eventBus.post( new ThreadDetectedEvent( MonitorEventManager.this ) );
            }
          },
          properties.getThreadInfoDetectedDelay(), TimeUnit.SECONDS );
    }

    if ( properties.isAllowMemoryDetected() ) {
      scheduleWorker.schedule( () -> {
            if ( properties.isTurnOn() ) {
              eventBus.post( new MemoryDetectedEvent( MonitorEventManager.this ) );
            }
          },
          properties.getMemoryInfoDetectedDelay(), TimeUnit.SECONDS );
    }

    if ( properties.isAllowGcInfoDetected() ) {
      NotificationListener notificationListener = ( notification, handback ) -> {

        GarbageCollectionNotificationInfo notificationInfo = GarbageCollectionNotificationInfo
            .from( ( CompositeData ) notification.getUserData() );

        if ( properties.isTurnOn() ) {
          eventBus.post( new GcDetectedEvent( MonitorEventManager.this, notificationInfo ) );
        }

      };

      for ( GarbageCollectorMXBean gcBean : ManagementFactory.getGarbageCollectorMXBeans() ) {
        ( ( NotificationEmitter ) gcBean ).addNotificationListener( notificationListener, null, null );
      }
    }
  }
}
