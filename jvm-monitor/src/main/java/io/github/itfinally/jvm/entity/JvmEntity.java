package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

@Table( name = "v1_jvm" )
public class JvmEntity extends BasicEntity<JvmArgumentsEntity> {

  /**
   * 虚拟机名称
   */

  private String name;

  /**
   * 虚拟机版本
   */

  private String version;

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

  @Column( name = "name", nullable = false )
  public String getName() {
    return name;
  }

  public JvmEntity setName( String name ) {
    this.name = name;
    return this;
  }

  @Column( name = "version", nullable = false )
  public String getVersion() {
    return version;
  }

  public JvmEntity setVersion( String version ) {
    this.version = version;
    return this;
  }

  @Column( name = "compiler", nullable = false )
  public String getCompiler() {
    return compiler;
  }

  public JvmEntity setCompiler( String compiler ) {
    this.compiler = compiler;
    return this;
  }

  @Column( name = "os_name", nullable = false )
  public String getOsName() {
    return osName;
  }

  public JvmEntity setOsName( String osName ) {
    this.osName = osName;
    return this;
  }

  @Column( name = "os_version", nullable = false )
  public String getOsVersion() {
    return osVersion;
  }

  public JvmEntity setOsVersion( String osVersion ) {
    this.osVersion = osVersion;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmEntity ) ) return false;
    JvmEntity that = ( JvmEntity ) o;

    return Objects.equals( getId(), that.getId() ) &&
        Objects.equals( getName(), that.getName() ) &&
        Objects.equals( getVersion(), that.getVersion() ) &&
        Objects.equals( getCompiler(), that.getCompiler() ) &&
        Objects.equals( getOsName(), that.getOsName() ) &&
        Objects.equals( getOsVersion(), that.getOsVersion() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getId(), getName(), getVersion(),
        getCompiler(), getOsName(), getOsVersion() );
  }

  @Override
  public String toString() {
    return "JvmEntity{" +

        "id=" + getId() +
        ", name='" + getName() + '\'' +
        ", version='" + getVersion() + '\'' +
        ", compiler='" + getCompiler() + '\'' +
        ", osName='" + getOsName() + '\'' +
        ", osVersion='" + getOsVersion() + '\'' +

        '}';
  }
}