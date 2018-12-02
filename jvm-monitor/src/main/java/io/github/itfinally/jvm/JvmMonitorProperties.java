package io.github.itfinally.jvm;

import io.github.itfinally.jvm.components.WorkerCodeProvider;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties( prefix = "monitor.java" )
public class JvmMonitorProperties {
  private String dataCentralHost;

  // True which mean system is in debug model, all functions are shutdown.
  private boolean turnOn = true;

  // Message will be send to central on batch when size arrive ${messageSendSize}
  private long messageSendSize = 32;

  // Message will be send to central on batch even if condition ${messageSendSize} is not satisfy
  private long messageIdleTime = 900;

  // Worker code provider
  private Class<? extends WorkerCodeProvider> workerCodeProvider;

  private boolean allowThreadDetected = true;

  private long threadInfoDetectedDelay = 3;

  private boolean allowMemoryDetected = true;

  private long memoryInfoDetectedDelay = 2;

  private boolean allowGcInfoDetected = true;

  public String getDataCentralHost() {
    return dataCentralHost;
  }

  public JvmMonitorProperties setDataCentralHost( String dataCentralHost ) {
    this.dataCentralHost = dataCentralHost;
    return this;
  }

  public boolean isTurnOn() {
    return turnOn;
  }

  public JvmMonitorProperties setTurnOn( boolean turnOn ) {
    this.turnOn = turnOn;
    return this;
  }

  public long getMessageSendSize() {
    return messageSendSize;
  }

  public JvmMonitorProperties setMessageSendSize( long messageSendSize ) {
    this.messageSendSize = messageSendSize;
    return this;
  }

  public long getMessageIdleTime() {
    return messageIdleTime;
  }

  public JvmMonitorProperties setMessageIdleTime( long messageIdleTime ) {
    this.messageIdleTime = messageIdleTime;
    return this;
  }

  public Class<? extends WorkerCodeProvider> getWorkerCodeProvider() {
    return workerCodeProvider;
  }

  public JvmMonitorProperties setWorkerCodeProvider( Class<? extends WorkerCodeProvider> workerCodeProvider ) {
    this.workerCodeProvider = workerCodeProvider;
    return this;
  }

  public boolean isAllowThreadDetected() {
    return allowThreadDetected;
  }

  public JvmMonitorProperties setAllowThreadDetected( boolean allowThreadDetected ) {
    this.allowThreadDetected = allowThreadDetected;
    return this;
  }

  public long getThreadInfoDetectedDelay() {
    return threadInfoDetectedDelay;
  }

  public JvmMonitorProperties setThreadInfoDetectedDelay( long threadInfoDetectedDelay ) {
    this.threadInfoDetectedDelay = threadInfoDetectedDelay;
    return this;
  }

  public boolean isAllowMemoryDetected() {
    return allowMemoryDetected;
  }

  public JvmMonitorProperties setAllowMemoryDetected( boolean allowMemoryDetected ) {
    this.allowMemoryDetected = allowMemoryDetected;
    return this;
  }

  public long getMemoryInfoDetectedDelay() {
    return memoryInfoDetectedDelay;
  }

  public JvmMonitorProperties setMemoryInfoDetectedDelay( long memoryInfoDetectedDelay ) {
    this.memoryInfoDetectedDelay = memoryInfoDetectedDelay;
    return this;
  }

  public boolean isAllowGcInfoDetected() {
    return allowGcInfoDetected;
  }

  public JvmMonitorProperties setAllowGcInfoDetected( boolean allowGcInfoDetected ) {
    this.allowGcInfoDetected = allowGcInfoDetected;
    return this;
  }
}
