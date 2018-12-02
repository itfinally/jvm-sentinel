package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

import java.util.Date;

@Table( name = "v1_jvm_thread" )
public class JvmThreadEntity extends BasicEntity<JvmArgumentsEntity> {

  /**
   * 线程快照时间
   */

  private Date createTime;

  /**
   * 当前存活的线程数( 含后台和非后台线程 )
   */

  private int threadCount;

  /**
   * 自虚拟机启动以来被创建及启动的线程数
   */

  private int startedThreadCount;

  /**
   * 当前存活的后台线程数
   */

  private int daemonThreadCount;

  /**
   * 自虚拟机启动或峰值重设以来最高存活的线程数
   */

  private int peakThreadCount;

  /**
   * 虚拟机Id
   */

  private long jvmId;

  @Column( name = "create_time", nullable = false )
  public Date getCreateTime() {
    return createTime;
  }

  public JvmThreadEntity setCreateTime( Date createTime ) {
    this.createTime = createTime;
    return this;
  }

  @Column( name = "thread_count", nullable = false )
  public int getThreadCount() {
    return threadCount;
  }

  public JvmThreadEntity setThreadCount( int threadCount ) {
    this.threadCount = threadCount;
    return this;
  }

  @Column( name = "started_thread_count", nullable = false )
  public int getStartedThreadCount() {
    return startedThreadCount;
  }

  public JvmThreadEntity setStartedThreadCount( int startedThreadCount ) {
    this.startedThreadCount = startedThreadCount;
    return this;
  }

  @Column( name = "daemon_thread_count", nullable = false )
  public int getDaemonThreadCount() {
    return daemonThreadCount;
  }

  public JvmThreadEntity setDaemonThreadCount( int daemonThreadCount ) {
    this.daemonThreadCount = daemonThreadCount;
    return this;
  }

  @Column( name = "peak_thread_count", nullable = false )
  public int getPeakThreadCount() {
    return peakThreadCount;
  }

  public JvmThreadEntity setPeakThreadCount( int peakThreadCount ) {
    this.peakThreadCount = peakThreadCount;
    return this;
  }

  @Column( name = "jvm_id", nullable = false )
  public long getJvmId() {
    return jvmId;
  }

  public JvmThreadEntity setJvmId( long jvmId ) {
    this.jvmId = jvmId;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmThreadEntity ) ) return false;
    JvmThreadEntity that = ( JvmThreadEntity ) o;

    return Objects.equals( getId(), that.getId() ) &&
        Objects.equals( getCreateTime(), that.getCreateTime() ) &&
        Objects.equals( getThreadCount(), that.getThreadCount() ) &&
        Objects.equals( getStartedThreadCount(), that.getStartedThreadCount() ) &&
        Objects.equals( getDaemonThreadCount(), that.getDaemonThreadCount() ) &&
        Objects.equals( getPeakThreadCount(), that.getPeakThreadCount() ) &&
        Objects.equals( getJvmId(), that.getJvmId() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getId(), getCreateTime(), getThreadCount(), getStartedThreadCount(), getDaemonThreadCount(), getPeakThreadCount(),
        getJvmId() );
  }

  @Override
  public String toString() {
    return "JvmThreadEntity{" +

        "id=" + getId() +
        ", createTime=" + getCreateTime() +
        ", threadCount=" + getThreadCount() +
        ", startedThreadCount=" + getStartedThreadCount() +
        ", daemonThreadCount=" + getDaemonThreadCount() +
        ", peakThreadCount=" + getPeakThreadCount() +
        ", jvmId=" + getJvmId() +

        '}';
  }
}