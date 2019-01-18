package io.github.itfinally.jvm;

import io.github.itfinally.jvm.requests.HttpSecurityFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Primary
@Configuration
@ConfigurationProperties( prefix = "monitor.java" )
public class JvmMonitorProperties implements GUIDProviderBuilder {

  // True which mean system is in debug model, all functions are shutdown.
  private boolean turnOn = true;

  // Thread information will be recorded if ${allowThreadDetected} is true
  private boolean allowThreadDetected = true;

  // Memory information will be recorded if ${allowMemoryDetected} is true
  private boolean allowMemoryDetected = true;

  // Collect virtual machine memory and threads information when gc has been active.
  private boolean allowGcInfoDetected = true;

  // Close VM if monitor initialize failed
  private boolean closeVMIfInitializeFailed = false;

  // Message will be send to central on batch when size arrive ${messageSendSize}
  private long messageSendSize = 32;

  // Message will be send to central on batch at every ${messageIdleTime} seconds
  // even if condition ${messageSendSize} is not satisfy.
  private long messageIdleTime = 900;

  // Memory detection interval, which mean thread information
  // has been collected at every ${memoryInfoDetectedDelay} seconds.
  private long memoryInfoDetectedDelay = 2;

  // Thread detection interval, which mean thread information
  // has been collected at every ${threadInfoDetectedDelay} seconds.
  private long threadInfoDetectedDelay = 3;

  // Data central host
  private String centralHost;

  // Data central security component, require if use 'https' protocol
  private HttpSecurityFactory httpSecurityFactory;

  // GUID provider
  private Class<? extends GUIDProvider> guidProvider;

  public boolean isTurnOn() {
    return turnOn;
  }

  public JvmMonitorProperties setTurnOn( boolean turnOn ) {
    this.turnOn = turnOn;
    return this;
  }

  public boolean isAllowThreadDetected() {
    return allowThreadDetected;
  }

  public JvmMonitorProperties setAllowThreadDetected( boolean allowThreadDetected ) {
    this.allowThreadDetected = allowThreadDetected;
    return this;
  }

  public boolean isAllowMemoryDetected() {
    return allowMemoryDetected;
  }

  public JvmMonitorProperties setAllowMemoryDetected( boolean allowMemoryDetected ) {
    this.allowMemoryDetected = allowMemoryDetected;
    return this;
  }

  public boolean isAllowGcInfoDetected() {
    return allowGcInfoDetected;
  }

  public JvmMonitorProperties setAllowGcInfoDetected( boolean allowGcInfoDetected ) {
    this.allowGcInfoDetected = allowGcInfoDetected;
    return this;
  }

  public boolean isCloseVMIfInitializeFailed() {
    return closeVMIfInitializeFailed;
  }

  public JvmMonitorProperties setCloseVMIfInitializeFailed( boolean closeVMIfInitializeFailed ) {
    this.closeVMIfInitializeFailed = closeVMIfInitializeFailed;
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

  public long getMemoryInfoDetectedDelay() {
    return memoryInfoDetectedDelay;
  }

  public JvmMonitorProperties setMemoryInfoDetectedDelay( long memoryInfoDetectedDelay ) {
    this.memoryInfoDetectedDelay = memoryInfoDetectedDelay;
    return this;
  }

  public long getThreadInfoDetectedDelay() {
    return threadInfoDetectedDelay;
  }

  public JvmMonitorProperties setThreadInfoDetectedDelay( long threadInfoDetectedDelay ) {
    this.threadInfoDetectedDelay = threadInfoDetectedDelay;
    return this;
  }

  public String getCentralHost() {
    return centralHost;
  }

  public JvmMonitorProperties setCentralHost( String centralHost ) {
    this.centralHost = centralHost;
    return this;
  }

  public HttpSecurityFactory getHttpSecurityFactory() {
    return httpSecurityFactory;
  }

  public JvmMonitorProperties setHttpSecurityFactory( HttpSecurityFactory httpSecurityFactory ) {
    this.httpSecurityFactory = httpSecurityFactory;
    return this;
  }

  public Class<? extends GUIDProvider> getGuidProvider() {
    return guidProvider;
  }

  public JvmMonitorProperties setGuidProvider( Class<? extends GUIDProvider> guidProvider ) {
    this.guidProvider = guidProvider;
    return this;
  }
}
