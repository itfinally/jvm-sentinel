package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

import java.util.Date;

@Table( name = "v1_jvm_status" )
public class JvmStatusEntity extends BasicEntity<JvmStatusEntity> {

  /**
   * 虚拟机 id, 使用 hash(ip:port) 作为 id 的值
   */

  private long jvmHashId;

  /**
   * 虚拟机启动时间点
   */

  private Date createTime = new Date();

  /**
   * 虚拟机名称
   */

  private String name;

  /**
   * 虚拟机版本
   */

  private String version;

  /**
   * 虚拟机使用的 Java 版本
   */

  private String javaVersion;

  /**
   * 编译器信息
   */

  private String compiler;

  /**
   * 操作系统名称
   */

  private String osName;

  /**
   * 操作系统版本
   */

  private String osVersion;

  @Column( name = "jvm_hash_id", nullable = false )
  public long getJvmHashId() {
    return jvmHashId;
  }

  public JvmStatusEntity setJvmHashId( long jvmHashId ) {
    this.jvmHashId = jvmHashId;
    return this;
  }

  @Column( name = "create_time", nullable = false )
  public Date getCreateTime() {
    return createTime;
  }

  public JvmStatusEntity setCreateTime( Date createTime ) {
    this.createTime = createTime;
    return this;
  }

  @Column( name = "name", nullable = false )
  public String getName() {
    return name;
  }

  public JvmStatusEntity setName( String name ) {
    this.name = name;
    return this;
  }

  @Column( name = "version", nullable = false )
  public String getVersion() {
    return version;
  }

  public JvmStatusEntity setVersion( String version ) {
    this.version = version;
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

  @Column( name = "compiler", nullable = false )
  public String getCompiler() {
    return compiler;
  }

  public JvmStatusEntity setCompiler( String compiler ) {
    this.compiler = compiler;
    return this;
  }

  @Column( name = "os_name", nullable = false )
  public String getOsName() {
    return osName;
  }

  public JvmStatusEntity setOsName( String osName ) {
    this.osName = osName;
    return this;
  }

  @Column( name = "os_version", nullable = false )
  public String getOsVersion() {
    return osVersion;
  }

  public JvmStatusEntity setOsVersion( String osVersion ) {
    this.osVersion = osVersion;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmStatusEntity ) ) return false;
    JvmStatusEntity that = ( JvmStatusEntity ) o;

    return Objects.equals( getId(), that.getId() ) &&
        Objects.equals( getJvmHashId(), that.getJvmHashId() ) &&
        Objects.equals( getCreateTime(), that.getCreateTime() ) &&
        Objects.equals( getName(), that.getName() ) &&
        Objects.equals( getVersion(), that.getVersion() ) &&
        Objects.equals( getJavaVersion(), that.getJavaVersion() ) &&
        Objects.equals( getCompiler(), that.getCompiler() ) &&
        Objects.equals( getOsName(), that.getOsName() ) &&
        Objects.equals( getOsVersion(), that.getOsVersion() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getId(), getJvmHashId(), getCreateTime(), getName(), getVersion(), getJavaVersion(),
        getCompiler(), getOsName(), getOsVersion() );
  }

  @Override
  public String toString() {
    return "JvmStatusEntity{" +

        "id=" + getId() +
        ", jvmHashId=" + getJvmHashId() +
        ", createTime=" + getCreateTime() +
        ", name='" + getName() + '\'' +
        ", version='" + getVersion() + '\'' +
        ", javaVersion='" + getJavaVersion() + '\'' +
        ", compiler='" + getCompiler() + '\'' +
        ", osName='" + getOsName() + '\'' +
        ", osVersion='" + getOsVersion() + '\'' +

        '}';
  }
}