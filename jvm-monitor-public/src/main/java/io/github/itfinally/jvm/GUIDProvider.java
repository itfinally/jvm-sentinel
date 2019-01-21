package io.github.itfinally.jvm;

import java.util.concurrent.ThreadLocalRandom;

public interface GUIDProvider {
  long getGUID();

  class DefaultGUIDProvider implements GUIDProvider {

    @Override
    public long getGUID() {
      return ThreadLocalRandom.current().nextLong( 0, Long.MAX_VALUE );
    }
  }
}