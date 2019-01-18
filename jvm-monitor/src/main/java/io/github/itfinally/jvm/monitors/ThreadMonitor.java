package io.github.itfinally.jvm.monitors;

import com.google.common.eventbus.Subscribe;
import io.github.itfinally.jvm.components.MachineIdInitializer;
import io.github.itfinally.jvm.components.events.MonitorEventManager;
import io.github.itfinally.jvm.components.events.ThreadDetectedEvent;
import io.github.itfinally.jvm.entity.JvmThreadEntity;
import io.github.itfinally.jvm.entity.JvmThreadInfoEntity;
import io.github.itfinally.jvm.entity.JvmThreadStackEntity;
import io.github.itfinally.jvm.requests.VManagerClient;
import io.github.itfinally.jvm.vo.ThreadInfoVo;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@Component
public class ThreadMonitor extends AbstractMonitorProcessor {
  private final AtomicBoolean isRunning = new AtomicBoolean( false );

  @Resource
  private VManagerClient vManagerClient;

  public ThreadMonitor( MonitorEventManager eventManager ) {
    eventManager.register( this );
  }

  public ThreadInfoVo getCurrentThreadInfos() {
    ThreadMXBean threadMXBean = ManagementFactory.getThreadMXBean();

    JvmThreadEntity jvmThreadEntity = new JvmThreadEntity()
        .setStartedThreadCount( threadMXBean.getTotalStartedThreadCount() )
        .setDaemonThreadCount( threadMXBean.getDaemonThreadCount() )
        .setPeakThreadCount( threadMXBean.getPeakThreadCount() )
        .setThreadCount( threadMXBean.getThreadCount() )
        .setJvmId( MachineIdInitializer.MACHINE_ID );

    ThreadInfo[] threadInfos = threadMXBean.dumpAllThreads( false, false );
    List<JvmThreadStackEntity> jvmThreadStackEntities = new ArrayList<>( threadInfos.length );

    // Assume that each thread has an average stack depth of 16
    List<JvmThreadInfoEntity> jvmThreadInfoEntities = new ArrayList<>( threadInfos.length * 16 );

    JvmThreadStackEntity jvmThreadStackEntity;
    JvmThreadInfoEntity jvmThreadInfoEntity;

    for ( ThreadInfo item : threadInfos ) {
      jvmThreadInfoEntity = new JvmThreadInfoEntity()
          .setState( item.getThreadState().name() )
          .setJvmThreadId( jvmThreadEntity.getId() )
          .setLockName( item.getLockName() )
          .setThreadName( item.getThreadName() )
          .setLockOwnerName( item.getLockOwnerName() )
          .setLockOwnerId( item.getLockOwnerId() )
          .setNativeThread( item.isInNative() )
          .setSuspended( item.isSuspended() );

      for ( StackTraceElement innerItem : item.getStackTrace() ) {
        jvmThreadStackEntity = new JvmThreadStackEntity()
            .setThreadInfoId( jvmThreadInfoEntity.getId() )
            .setClassName( innerItem.getClassName() )
            .setFileName( innerItem.getFileName() )
            .setLineNumber( innerItem.getLineNumber() )
            .setMethodName( innerItem.getMethodName() );

        jvmThreadStackEntities.add( jvmThreadStackEntity );
      }

      jvmThreadInfoEntities.add( jvmThreadInfoEntity );
    }

    return new ThreadInfoVo( jvmThreadEntity, jvmThreadInfoEntities, jvmThreadStackEntities );
  }

  @Subscribe
  private void threadInfoDetected( ThreadDetectedEvent ignore ) {
    if ( !isRunning.compareAndSet( false, true ) ) {
      return;
    }

    try {
      sendData( vManagerClient.saveThreadInfos( getCurrentThreadInfos() ) );

    } finally {
      isRunning.compareAndSet( true, false );
    }
  }
}
