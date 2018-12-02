package io.github.itfinally.jvm.entity;

import io.github.itfinally.jvm.components.GUIDBuilder;
import org.springframework.stereotype.Component;

@SuppressWarnings( "unchecked" )
public class BasicEntity<Entity extends BasicEntity<Entity>> {
  private long id = GUIDProvider.guidBuilder.getId();

  public long getId() {
    return id;
  }

  public Entity setId( long id ) {
    this.id = id;
    return ( Entity ) this;
  }

  @Component
  protected static class GUIDProvider {
    private static GUIDBuilder guidBuilder;

    public GUIDProvider( GUIDBuilder guidBuilder ) {
      GUIDProvider.guidBuilder = guidBuilder;
    }
  }
}
