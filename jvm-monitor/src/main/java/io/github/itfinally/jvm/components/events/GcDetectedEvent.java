package io.github.itfinally.jvm.components.events;

import com.sun.management.GarbageCollectionNotificationInfo;
import org.springframework.context.ApplicationEvent;

public class GcDetectedEvent extends ApplicationEvent {
  private final GarbageCollectionNotificationInfo notificationInfo;

  public GcDetectedEvent( Object source, GarbageCollectionNotificationInfo notificationInfo ) {
    super( source );

    this.notificationInfo = notificationInfo;
  }

  public GarbageCollectionNotificationInfo getNotificationInfo() {
    return notificationInfo;
  }
}
