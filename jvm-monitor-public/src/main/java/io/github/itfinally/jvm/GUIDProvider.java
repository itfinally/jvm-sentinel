package io.github.itfinally.jvm;

public interface GUIDProvider {
  long getGUID();

  class DefaultGUIDProvider implements GUIDProvider {

    @Override
    public long getGUID() {
      return -1;
    }
  }
}