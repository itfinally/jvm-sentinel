package io.github.itfinally.jvm.components.events;

import io.github.itfinally.jvm.JvmMonitorProperties;
import org.springframework.context.ApplicationEvent;

public class MonitorStartingEvent extends ApplicationEvent {
  private final JvmMonitorProperties properties;

  public MonitorStartingEvent( Object source, JvmMonitorProperties properties ) {
    super( source );

    this.properties = properties;
  }

  public JvmMonitorProperties getProperties() {
    return properties;
  }
}
