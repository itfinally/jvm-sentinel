package io.github.itfinally.jvm.entity;

import io.github.itfinally.exception.MethodInvokeRuntimeException;
import io.github.itfinally.jvm.GUIDProvider;
import io.github.itfinally.jvm.GUIDProviderBuilder;
import org.springframework.stereotype.Component;

import javax.persistence.Column;
import javax.persistence.Id;

@SuppressWarnings( "unchecked" )
public class BasicEntity<Entity extends BasicEntity<Entity>> {
  private long id = GUIDProviderInjector.guidProvider.getGUID();

  @Id
  @Column( name = "id" )
  public long getId() {
    return id;
  }

  public Entity setId( long id ) {
    this.id = id;
    return ( Entity ) this;
  }

  @Component
  protected static class GUIDProviderInjector {
    private static GUIDProvider guidProvider;

    public GUIDProviderInjector( GUIDProviderBuilder guidProviderBuilder ) {
      Class<? extends GUIDProvider> clazz = guidProviderBuilder.getGuidProvider();
      if ( null == clazz ) {
        throw new NullPointerException( "Expect a GUID provider but got null." );
      }

      try {
        guidProvider = clazz.newInstance();

      } catch ( InstantiationException | IllegalAccessException e ) {
        throw new MethodInvokeRuntimeException( e );
      }
    }
  }
}
