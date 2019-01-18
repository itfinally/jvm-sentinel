package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

import java.util.Date;

@Table( name = "v1_jvm_thread_info" )
public class JvmThreadInfoEntity extends BasicEntity<JvmThreadInfoEntity> {

  /**
   * 线程名称
   */

  private String threadName;

  /**
   * 线程快照时间
   */

  private Date createTime = new Date();

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

  private long lockOwnerId;

  /**
   * 持有当前线程需要获取的锁的线程名称
   */

  private String lockOwnerName;

  /**
   * 虚拟机线程状态 id
   */

  private long jvmThreadId;

  @Column( name = "thread_name", nullable = false )
  public String getThreadName() {
    return threadName;
  }

  public JvmThreadInfoEntity setThreadName( String threadName ) {
    this.threadName = threadName;
    return this;
  }

  @Column( name = "create_time", nullable = false )
  public Date getCreateTime() {
    return createTime;
  }

  public JvmThreadInfoEntity setCreateTime( Date createTime ) {
    this.createTime = createTime;
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
  public long getLockOwnerId() {
    return lockOwnerId;
  }

  public JvmThreadInfoEntity setLockOwnerId( long lockOwnerId ) {
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

  @Column( name = "jvm_thread_id", nullable = false )
  public long getJvmThreadId() {
    return jvmThreadId;
  }

  public JvmThreadInfoEntity setJvmThreadId( long jvmThreadId ) {
    this.jvmThreadId = jvmThreadId;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmThreadInfoEntity ) ) return false;
    JvmThreadInfoEntity that = ( JvmThreadInfoEntity ) o;

    return Objects.equals( getId(), that.getId() ) &&
        Objects.equals( getThreadName(), that.getThreadName() ) &&
        Objects.equals( getCreateTime(), that.getCreateTime() ) &&
        Objects.equals( isNativeThread(), that.isNativeThread() ) &&
        Objects.equals( isSuspended(), that.isSuspended() ) &&
        Objects.equals( getState(), that.getState() ) &&
        Objects.equals( getLockName(), that.getLockName() ) &&
        Objects.equals( getLockOwnerId(), that.getLockOwnerId() ) &&
        Objects.equals( getLockOwnerName(), that.getLockOwnerName() ) &&
        Objects.equals( getJvmThreadId(), that.getJvmThreadId() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getId(), getThreadName(), getCreateTime(), isNativeThread(), isSuspended(), getState(),
        getLockName(), getLockOwnerId(), getLockOwnerName(), getJvmThreadId() );
  }

  @Override
  public String toString() {
    return "JvmThreadInfoEntity{" +

        "id=" + getId() +
        ", threadName='" + getThreadName() + '\'' +
        ", createTime=" + getCreateTime() +
        ", nativeThread=" + isNativeThread() +
        ", suspended=" + isSuspended() +
        ", state='" + getState() + '\'' +
        ", lockName='" + getLockName() + '\'' +
        ", lockOwnerId=" + getLockOwnerId() +
        ", lockOwnerName='" + getLockOwnerName() + '\'' +
        ", jvmThreadId=" + getJvmThreadId() +

        '}';
  }
}