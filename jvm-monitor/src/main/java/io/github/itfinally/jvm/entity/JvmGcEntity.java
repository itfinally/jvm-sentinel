package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

import java.util.Date;

@Table( name = "v1_jvm_gc" )
public class JvmGcEntity extends BasicEntity<JvmArgumentsEntity> {

  /**
   * gc 快照时间
   */

  private Date createTime;

  /**
   * gc 名称
   */

  private String actionName;

  /**
   * gc 触发原因
   */

  private String cause;

  /**
   * gc 距离虚拟机启动时间的毫秒值
   */

  private long startTime;

  /**
   * gc 执行耗时的毫秒值
   */

  private long duration;

  /**
   * gc 线程数
   */

  private int threadCount;

  /**
   * 虚拟机Id
   */

  private long jvmId;

  @Column( name = "create_time", nullable = false )
  public Date getCreateTime() {
    return createTime;
  }

  public JvmGcEntity setCreateTime( Date createTime ) {
    this.createTime = createTime;
    return this;
  }

  @Column( name = "action_name", nullable = false )
  public String getActionName() {
    return actionName;
  }

  public JvmGcEntity setActionName( String actionName ) {
    this.actionName = actionName;
    return this;
  }

  @Column( name = "cause", nullable = false )
  public String getCause() {
    return cause;
  }

  public JvmGcEntity setCause( String cause ) {
    this.cause = cause;
    return this;
  }

  @Column( name = "start_time", nullable = false )
  public long getStartTime() {
    return startTime;
  }

  public JvmGcEntity setStartTime( long startTime ) {
    this.startTime = startTime;
    return this;
  }

  @Column( name = "duration", nullable = false )
  public long getDuration() {
    return duration;
  }

  public JvmGcEntity setDuration( long duration ) {
    this.duration = duration;
    return this;
  }

  @Column( name = "thread_count", nullable = false )
  public int getThreadCount() {
    return threadCount;
  }

  public JvmGcEntity setThreadCount( int threadCount ) {
    this.threadCount = threadCount;
    return this;
  }

  @Column( name = "jvm_id", nullable = false )
  public long getJvmId() {
    return jvmId;
  }

  public JvmGcEntity setJvmId( long jvmId ) {
    this.jvmId = jvmId;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmGcEntity ) ) return false;
    JvmGcEntity that = ( JvmGcEntity ) o;

    return Objects.equals( getId(), that.getId() ) &&
        Objects.equals( getCreateTime(), that.getCreateTime() ) &&
        Objects.equals( getActionName(), that.getActionName() ) &&
        Objects.equals( getCause(), that.getCause() ) &&
        Objects.equals( getStartTime(), that.getStartTime() ) &&
        Objects.equals( getDuration(), that.getDuration() ) &&
        Objects.equals( getThreadCount(), that.getThreadCount() ) &&
        Objects.equals( getJvmId(), that.getJvmId() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getId(), getCreateTime(), getActionName(), getCause(), getStartTime(), getDuration(),
        getThreadCount(), getJvmId() );
  }

  @Override
  public String toString() {
    return "JvmGcEntity{" +

        "id=" + getId() +
        ", createTime=" + getCreateTime() +
        ", actionName='" + getActionName() + '\'' +
        ", cause='" + getCause() + '\'' +
        ", startTime=" + getStartTime() +
        ", duration=" + getDuration() +
        ", threadCount=" + getThreadCount() +
        ", jvmId=" + getJvmId() +

        '}';
  }
}