package io.github.itfinally.jvm.monitors;

import com.google.common.eventbus.Subscribe;
import com.sun.management.GarbageCollectionNotificationInfo;
import com.sun.management.GcInfo;
import io.github.itfinally.jvm.components.AbstractMachineIdInitializer;
import io.github.itfinally.jvm.components.events.GcDetectedEvent;
import io.github.itfinally.jvm.components.events.MonitorEventManager;
import io.github.itfinally.jvm.entity.JvmGcEntity;
import io.github.itfinally.jvm.entity.JvmMemoryEntity;
import io.github.itfinally.jvm.requests.VManagerClient;
import io.github.itfinally.jvm.vo.GcInfoVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.util.stream.Collectors.toList;

@Component
@ConditionalOnProperty( prefix = "monitor.java", value = "deliver-on-local", havingValue = "false" )
public class GcMonitor extends AbstractMonitorProcessor {
  private final AtomicBoolean isRunning = new AtomicBoolean( false );

  private VManagerClient managerClient;

  public GcMonitor( MonitorEventManager eventManager ) {
    eventManager.register( this );
  }

  @Autowired( required = false )
  public GcMonitor setManagerClient( VManagerClient managerClient ) {
    this.managerClient = managerClient;
    return this;
  }

  public GcInfoVo getGcInfos( GarbageCollectionNotificationInfo notificationInfo ) {
    GcInfo gcInfo = notificationInfo.getGcInfo();

    JvmGcEntity jvmGcEntity = new JvmGcEntity()
        .setJvmId( AbstractMachineIdInitializer.machineId() )
        .setActionName( notificationInfo.getGcAction() )
        .setCause( notificationInfo.getGcCause() )
        .setDuration( gcInfo.getDuration() )
        .setThreadCount( Integer.parseInt( notificationInfo.getGcInfo().get( "GcThreadCount" ).toString() ) )
        .setTimeGap( ManagementFactory.getRuntimeMXBean().getStartTime() );

    List<JvmMemoryEntity> beforeGcMemoryEntities = gcInfo.getMemoryUsageBeforeGc()
        .entrySet().stream().map( entry -> {
          MemoryUsage memoryUsage = entry.getValue();

          return new JvmMemoryEntity()
              .setJvmGcId( AbstractMachineIdInitializer.machineId() )
              .setMemorySpaceName( entry.getKey() )
              .setJvmGcId( jvmGcEntity.getId() )
              .setSnapshotForGc( true )
              .setCommitted( memoryUsage.getCommitted() )
              .setInit( memoryUsage.getInit() )
              .setMax( memoryUsage.getMax() )
              .setUsed( memoryUsage.getUsed() )
              .setHappenBeforeGc( true );

        } ).collect( toList() );

    List<JvmMemoryEntity> afterGcMemoryEntities = gcInfo.getMemoryUsageAfterGc()
        .entrySet().stream().map( entry -> {
          MemoryUsage memoryUsage = entry.getValue();

          return new JvmMemoryEntity()
              .setJvmGcId( AbstractMachineIdInitializer.machineId() )
              .setMemorySpaceName( entry.getKey() )
              .setJvmGcId( jvmGcEntity.getId() )
              .setSnapshotForGc( true )
              .setCommitted( memoryUsage.getCommitted() )
              .setInit( memoryUsage.getInit() )
              .setMax( memoryUsage.getMax() )
              .setUsed( memoryUsage.getUsed() )
              .setHappenBeforeGc( false );

        } ).collect( toList() );

    return new GcInfoVo( jvmGcEntity, beforeGcMemoryEntities, afterGcMemoryEntities );
  }

  @Subscribe
  protected void memoryInfoDetected( GcDetectedEvent event ) {
    if ( !isRunning.compareAndSet( false, true ) ) {
      return;
    }

    try {
      deliverDataByAsyncHttp( managerClient.saveGcInfos( getGcInfos( event.getNotificationInfo() ) ) );

    } finally {
      isRunning.compareAndSet( true, false );
    }
  }
}
