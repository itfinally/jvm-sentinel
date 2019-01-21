package io.github.itfinally.jvm.monitor;

import com.google.common.eventbus.Subscribe;
import io.github.itfinally.jvm.components.events.GcDetectedEvent;
import io.github.itfinally.jvm.components.events.MonitorEventManager;
import io.github.itfinally.jvm.monitors.GcMonitor;
import io.github.itfinally.jvm.repository.MonitorDataSavingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@ConditionalOnProperty( prefix = "monitor.java", value = "deliver-on-local", havingValue = "true" )
public class LocalGcMonitor extends GcMonitor {

  @Resource
  private MonitorDataSavingService monitorDataSavingService;

  public LocalGcMonitor( MonitorEventManager eventManager ) {
    super( eventManager );
  }

  @Override
  @Subscribe
  protected void memoryInfoDetected( GcDetectedEvent event ) {
    monitorDataSavingService.saveGcInfos( getGcInfos( event.getNotificationInfo() ) );
  }
}
