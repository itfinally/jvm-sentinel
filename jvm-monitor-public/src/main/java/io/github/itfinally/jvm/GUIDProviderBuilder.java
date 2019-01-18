package io.github.itfinally.jvm;

import org.springframework.stereotype.Component;

public interface GUIDProviderBuilder {
  Class<? extends GUIDProvider> getGuidProvider();

  @Component
  class DefaultGUIDProviderBuilder implements GUIDProviderBuilder {

    @Override
    public Class<? extends GUIDProvider> getGuidProvider() {
      return GUIDProvider.DefaultGUIDProvider.class;
    }
  }
}
