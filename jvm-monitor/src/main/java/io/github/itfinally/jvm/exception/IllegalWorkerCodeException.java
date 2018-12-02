package io.github.itfinally.jvm.exception;

public class IllegalWorkerCodeException extends RuntimeException {
  public IllegalWorkerCodeException() {
  }

  public IllegalWorkerCodeException( String message ) {
    super( message );
  }

  public IllegalWorkerCodeException( String message, Throwable cause ) {
    super( message, cause );
  }

  public IllegalWorkerCodeException( Throwable cause ) {
    super( cause );
  }

  public IllegalWorkerCodeException( String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace ) {
    super( message, cause, enableSuppression, writableStackTrace );
  }
}
