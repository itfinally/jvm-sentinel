package io.github.itfinally.jvm.monitors;

import com.google.common.eventbus.Subscribe;
import com.sun.management.GarbageCollectionNotificationInfo;
import io.github.itfinally.jvm.components.events.GcDetectedEvent;
import io.github.itfinally.jvm.requests.VManagerClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class GcMonitor extends AbstractMonitorProcessor {
  private final AtomicBoolean isRunning = new AtomicBoolean( false );

  @Resource
  private VManagerClient vManagerClient;

  public void getGcInfos( GarbageCollectionNotificationInfo notificationInfo ) {
    notificationInfo.getGcInfo();
  }

  @Subscribe
  private void memoryInfoDetected( GcDetectedEvent event ) {
    if ( !isRunning.compareAndSet( false, true ) ) {
      return;
    }

    try {
      event.getNotificationInfo().getGcInfo();
//      sendData( vManagerClient.saveGcInfos( getGcInfos( event.getNotificationInfo() ) ) );

    } finally {
      isRunning.compareAndSet( true, false );
    }
  }
}
