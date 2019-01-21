package io.github.itfinally.jvm.monitor;

import com.google.common.eventbus.Subscribe;
import io.github.itfinally.jvm.components.events.MonitorEventManager;
import io.github.itfinally.jvm.components.events.ThreadDetectedEvent;
import io.github.itfinally.jvm.monitors.ThreadMonitor;
import io.github.itfinally.jvm.repository.MonitorDataSavingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@ConditionalOnProperty( prefix = "monitor.java", value = "deliver-on-local", havingValue = "true" )
public class LocalThreadMonitor extends ThreadMonitor {

  @Resource
  private MonitorDataSavingService monitorDataSavingService;

  public LocalThreadMonitor( MonitorEventManager eventManager ) {
    super( eventManager );
  }

  @Override
  @Subscribe
  protected void threadInfoDetected( ThreadDetectedEvent ignore ) {
    monitorDataSavingService.saveThreadInfos( getCurrentThreadInfos() );
  }
}
