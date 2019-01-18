package io.github.itfinally.jvm.components.events;

import org.springframework.context.ApplicationEvent;

public class ThreadDetectedEvent extends ApplicationEvent {
  public ThreadDetectedEvent( Object source ) {
    super( source );
  }
}
