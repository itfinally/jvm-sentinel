package io.github.itfinally.jvm.exception;

public class UnknownHostRuntimeException extends RuntimeException {
  public UnknownHostRuntimeException() {
  }

  public UnknownHostRuntimeException( String message ) {
    super( message );
  }

  public UnknownHostRuntimeException( String message, Throwable cause ) {
    super( message, cause );
  }

  public UnknownHostRuntimeException( Throwable cause ) {
    super( cause );
  }

  public UnknownHostRuntimeException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
    super( message, cause, enableSuppression, writableStackTrace );
  }
}
