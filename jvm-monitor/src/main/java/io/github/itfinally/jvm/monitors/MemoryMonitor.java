package io.github.itfinally.jvm.monitors;

import com.google.common.collect.Lists;
import com.google.common.eventbus.Subscribe;
import io.github.itfinally.jvm.commons.MemoryBlock;
import io.github.itfinally.jvm.components.MachineIdInitializer;
import io.github.itfinally.jvm.components.events.MemoryDetectedEvent;
import io.github.itfinally.jvm.components.events.MonitorEventManager;
import io.github.itfinally.jvm.entity.JvmMemoryEntity;
import io.github.itfinally.jvm.requests.VManagerClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class MemoryMonitor extends AbstractMonitorProcessor {
  private final AtomicBoolean isRunning = new AtomicBoolean( false );

  @Resource
  private VManagerClient vManagerClient;

  public MemoryMonitor( MonitorEventManager eventManager ) {
    eventManager.register( this );
  }

  public List<JvmMemoryEntity> getCurrentMemoryInfos( boolean isSnapshotForGc, long jvmGcId ) {
    MemoryMXBean memoryMXBean = ManagementFactory.getMemoryMXBean();
    List<MemoryPoolMXBean> memoryPoolMXBeans = ManagementFactory.getMemoryPoolMXBeans();

    MemoryUsage heapMemory = memoryMXBean.getHeapMemoryUsage();
    MemoryUsage nonHeapMemory = memoryMXBean.getNonHeapMemoryUsage();

    JvmMemoryEntity heapMemoryEntity = new JvmMemoryEntity()
        .setJvmGcId( isSnapshotForGc ? jvmGcId : -1 )
        .setSnapshotForGc( isSnapshotForGc )
        .setJvmId( MachineIdInitializer.MACHINE_ID )
        .setMemorySpaceName( MemoryBlock.HEAP.toString() )
        .setCommitted( heapMemory.getCommitted() )
        .setInit( heapMemory.getInit() )
        .setMax( heapMemory.getMax() )
        .setUsed( heapMemory.getUsed() );

    JvmMemoryEntity nonHeapMemoryEntity = new JvmMemoryEntity()
        .setJvmGcId( isSnapshotForGc ? jvmGcId : -1 )
        .setSnapshotForGc( isSnapshotForGc )
        .setJvmId( MachineIdInitializer.MACHINE_ID )
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
          .setJvmId( MachineIdInitializer.MACHINE_ID )
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
  private void memoryInfoDetected( MemoryDetectedEvent event ) {
    if ( !isRunning.compareAndSet( false, true ) ) {
      return;
    }

    try {
      sendData( vManagerClient.saveMemoryInfos( getCurrentMemoryInfos( false, -1 ) ) );

    } finally {
      isRunning.compareAndSet( true, false );
    }
  }
}
