package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

@Table( name = "v1_jvm_thread_info" )
public class JvmThreadInfoEntity extends BasicEntity<JvmArgumentsEntity> {

  /**
   * 线程名称
   */

  private String threadName;

  /**
   * 是否本地线程
   */

  private boolean nativeThread;

  /**
   * 是否已暂停
   */

  private boolean suspended;

  /**
   * 线程状态
   */

  private String state;

  /**
   * 持有的锁的名字
   */

  private String lockName;

  /**
   * 持有当前线程需要获取的锁的线程Id
   */

  private int lockOwnerId;

  /**
   * 持有当前线程需要获取的锁的线程名称
   */

  private String lockOwnerName;

  @Column( name = "thread_name", nullable = false )
  public String getThreadName() {
    return threadName;
  }

  public JvmThreadInfoEntity setThreadName( String threadName ) {
    this.threadName = threadName;
    return this;
  }

  @Column( name = "is_native_thread", nullable = false )
  public boolean isNativeThread() {
    return nativeThread;
  }

  public JvmThreadInfoEntity setNativeThread( boolean nativeThread ) {
    this.nativeThread = nativeThread;
    return this;
  }

  @Column( name = "is_suspended", nullable = false )
  public boolean isSuspended() {
    return suspended;
  }

  public JvmThreadInfoEntity setSuspended( boolean suspended ) {
    this.suspended = suspended;
    return this;
  }

  @Column( name = "state", nullable = false )
  public String getState() {
    return state;
  }

  public JvmThreadInfoEntity setState( String state ) {
    this.state = state;
    return this;
  }

  @Column( name = "lock_name", nullable = false )
  public String getLockName() {
    return lockName;
  }

  public JvmThreadInfoEntity setLockName( String lockName ) {
    this.lockName = lockName;
    return this;
  }

  @Column( name = "lock_owner_id", nullable = false )
  public int getLockOwnerId() {
    return lockOwnerId;
  }

  public JvmThreadInfoEntity setLockOwnerId( int lockOwnerId ) {
    this.lockOwnerId = lockOwnerId;
    return this;
  }

  @Column( name = "lock_owner_name", nullable = false )
  public String getLockOwnerName() {
    return lockOwnerName;
  }

  public JvmThreadInfoEntity setLockOwnerName( String lockOwnerName ) {
    this.lockOwnerName = lockOwnerName;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmThreadInfoEntity ) ) return false;
    JvmThreadInfoEntity that = ( JvmThreadInfoEntity ) o;

    return Objects.equals( getId(), that.getId() ) &&
        Objects.equals( getThreadName(), that.getThreadName() ) &&
        Objects.equals( isNativeThread(), that.isNativeThread() ) &&
        Objects.equals( isSuspended(), that.isSuspended() ) &&
        Objects.equals( getState(), that.getState() ) &&
        Objects.equals( getLockName(), that.getLockName() ) &&
        Objects.equals( getLockOwnerId(), that.getLockOwnerId() ) &&
        Objects.equals( getLockOwnerName(), that.getLockOwnerName() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getId(), getThreadName(), isNativeThread(), isSuspended(), getState(), getLockName(),
        getLockOwnerId(), getLockOwnerName() );
  }

  @Override
  public String toString() {
    return "JvmThreadInfoEntity{" +

        "id=" + getId() +
        ", threadName='" + getThreadName() + '\'' +
        ", nativeThread=" + isNativeThread() +
        ", suspended=" + isSuspended() +
        ", state='" + getState() + '\'' +
        ", lockName='" + getLockName() + '\'' +
        ", lockOwnerId=" + getLockOwnerId() +
        ", lockOwnerName='" + getLockOwnerName() + '\'' +

        '}';
  }
}