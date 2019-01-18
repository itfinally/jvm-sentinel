package io.github.itfinally.jvm.components.events;

import org.springframework.context.ApplicationEvent;

public class MemoryDetectedEvent extends ApplicationEvent {

  public MemoryDetectedEvent( Object source ) {
    super( source );
  }
}
