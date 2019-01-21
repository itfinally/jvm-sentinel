package io.github.itfinally.jvm.monitor;

import com.google.common.eventbus.Subscribe;
import io.github.itfinally.jvm.components.events.MemoryDetectedEvent;
import io.github.itfinally.jvm.components.events.MonitorEventManager;
import io.github.itfinally.jvm.monitors.MemoryMonitor;
import io.github.itfinally.jvm.repository.MonitorDataSavingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
@ConditionalOnProperty( prefix = "monitor.java", value = "deliver-on-local", havingValue = "true" )
public class LocalMemoryMonitor extends MemoryMonitor {

  @Resource
  private MonitorDataSavingService monitorDataSavingService;

  public LocalMemoryMonitor( MonitorEventManager eventManager ) {
    super( eventManager );
  }

  @Override
  @Subscribe
  protected void memoryInfoDetected( MemoryDetectedEvent event ) {
    monitorDataSavingService.saveMemoryInfos( getCurrentMemoryInfos( false, -1 ) );
  }
}
