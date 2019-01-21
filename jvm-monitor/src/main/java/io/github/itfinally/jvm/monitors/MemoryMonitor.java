package io.github.itfinally.jvm.monitors;

import com.google.common.eventbus.Subscribe;
import io.github.itfinally.jvm.commons.MemoryBlock;
import io.github.itfinally.jvm.components.AbstractMachineIdInitializer;
import io.github.itfinally.jvm.components.events.MemoryDetectedEvent;
import io.github.itfinally.jvm.components.events.MonitorEventManager;
import io.github.itfinally.jvm.entity.JvmMemoryEntity;
import io.github.itfinally.jvm.requests.VManagerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
@ConditionalOnProperty( prefix = "monitor.java", value = "deliver-on-local", havingValue = "false" )
public class MemoryMonitor extends AbstractMonitorProcessor {
  private final AtomicBoolean isRunning = new AtomicBoolean( false );

  private VManagerClient managerClient;

  public MemoryMonitor( MonitorEventManager eventManager ) {
    eventManager.register( this );
  }

  @Autowired( required = false )
  public MemoryMonitor setManagerClient( VManagerClient managerClient ) {
    this.managerClient = managerClient;
    return this;
  }

  public List<JvmMemoryEntity> getCurrentMemoryInfos( boolean isSnapshotForGc, long jvmGcId ) {
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

    MemoryUsage heapMemory = memoryMXBean.getHeapMemoryUsage();
    MemoryUsage nonHeapMemory = memoryMXBean.getNonHeapMemoryUsage();

    JvmMemoryEntity heapMemoryEntity = new JvmMemoryEntity()
        .setJvmGcId( isSnapshotForGc ? jvmGcId : -1 )
        .setSnapshotForGc( isSnapshotForGc )
        .setJvmId( AbstractMachineIdInitializer.machineId() )
        .setMemorySpaceName( MemoryBlock.HEAP.toString() )
        .setCommitted( heapMemory.getCommitted() )
        .setInit( heapMemory.getInit() )
        .setMax( heapMemory.getMax() )
        .setUsed( heapMemory.getUsed() );

    JvmMemoryEntity nonHeapMemoryEntity = new JvmMemoryEntity()
        .setJvmGcId( isSnapshotForGc ? jvmGcId : -1 )
        .setSnapshotForGc( isSnapshotForGc )
        .setJvmId( AbstractMachineIdInitializer.machineId() )
        .setMemorySpaceName( MemoryBlock.NON_HEAP.toString() )
        .setCommitted( nonHeapMemory.getCommitted() )
        .setInit( nonHeapMemory.getInit() )
        .setMax( nonHeapMemory.getMax() )
        .setUsed( nonHeapMemory.getUsed() );

    MemoryUsage memoryUsage;
    List<JvmMemoryEntity> jvmMemoryEntities = new ArrayList<>( memoryPoolMXBeans.size() + 2 );

    for ( MemoryPoolMXBean memoryPoolMXBean : memoryPoolMXBeans ) {
      memoryUsage = memoryPoolMXBean.getUsage();

      if ( null == memoryUsage ) {
        continue;
      }

      jvmMemoryEntities.add( new JvmMemoryEntity()
          .setMemorySpaceName( memoryPoolMXBean.getName() )
          .setJvmGcId( isSnapshotForGc ? jvmGcId : -1 )
          .setJvmId( AbstractMachineIdInitializer.machineId() )
          .setSnapshotForGc( isSnapshotForGc )
          .setCommitted( memoryUsage.getCommitted() )
          .setInit( memoryUsage.getInit() )
          .setMax( memoryUsage.getMax() )
          .setUsed( memoryUsage.getUsed() ) );
    }

    jvmMemoryEntities.add( heapMemoryEntity );
    jvmMemoryEntities.add( nonHeapMemoryEntity );

    return jvmMemoryEntities;
  }

  @Subscribe
  protected void memoryInfoDetected( MemoryDetectedEvent event ) {
    if ( !isRunning.compareAndSet( false, true ) ) {
      return;
    }

    try {
      deliverDataByAsyncHttp( managerClient.saveMemoryInfos( getCurrentMemoryInfos( false, -1 ) ) );

    } finally {
      isRunning.compareAndSet( true, false );
    }
  }
}
