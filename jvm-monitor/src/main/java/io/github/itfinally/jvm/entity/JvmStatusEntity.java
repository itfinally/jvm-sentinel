package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

import java.util.Date;

@Table( name = "v1_jvm_status" )
public class JvmStatusEntity extends BasicEntity<JvmArgumentsEntity> {

  /**
   * 虚拟机Id
   */

  private long jvmId;

  /**
   * 虚拟机启动时间点
   */

  private Date startTime;

  /**
   * 虚拟机使用的 Java 版本
   */

  private String javaVersion;

  @Column( name = "jvm_id", nullable = false )
  public long getJvmId() {
    return jvmId;
  }

  public JvmStatusEntity setJvmId( long jvmId ) {
    this.jvmId = jvmId;
    return this;
  }

  @Column( name = "start_time", nullable = false )
  public Date getStartTime() {
    return startTime;
  }

  public JvmStatusEntity setStartTime( Date startTime ) {
    this.startTime = startTime;
    return this;
  }

  @Column( name = "java_version", nullable = false )
  public String getJavaVersion() {
    return javaVersion;
  }

  public JvmStatusEntity setJavaVersion( String javaVersion ) {
    this.javaVersion = javaVersion;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmStatusEntity ) ) return false;
    JvmStatusEntity that = ( JvmStatusEntity ) o;

    return Objects.equals( getId(), that.getId() ) &&
        Objects.equals( getJvmId(), that.getJvmId() ) &&
        Objects.equals( getStartTime(), that.getStartTime() ) &&
        Objects.equals( getJavaVersion(), that.getJavaVersion() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getId(), getJvmId(), getStartTime(), getJavaVersion() );
  }

  @Override
  public String toString() {
    return "JvmStatusEntity{" +

        "id=" + getId() +
        ", jvmId=" + getJvmId() +
        ", startTime=" + getStartTime() +
        ", javaVersion='" + getJavaVersion() + '\'' +

        '}';
  }
}