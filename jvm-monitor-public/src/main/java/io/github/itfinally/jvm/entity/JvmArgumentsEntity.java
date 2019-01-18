package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

@Table( name = "v1_jvm_arguments" )
public class JvmArgumentsEntity extends BasicEntity<JvmArgumentsEntity> {

  /**
   * 虚拟机启动记录Id
   */

  private long jvmId;

  /**
   * 虚拟机启动时使用的参数
   */

  private String value;

  @Column( name = "jvm_id", nullable = false )
  public long getJvmId() {
    return jvmId;
  }

  public JvmArgumentsEntity setJvmId( long jvmId ) {
    this.jvmId = jvmId;
    return this;
  }

  @Column( name = "value", nullable = false )
  public String getValue() {
    return value;
  }

  public JvmArgumentsEntity setValue( String value ) {
    this.value = value;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmArgumentsEntity ) ) return false;
    JvmArgumentsEntity that = ( JvmArgumentsEntity ) o;

    return Objects.equals( getId(), that.getId() ) &&
        Objects.equals( getJvmId(), that.getJvmId() ) &&
        Objects.equals( getValue(), that.getValue() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getId(), getJvmId(), getValue() );
  }

  @Override
  public String toString() {
    return "JvmArgumentsEntity{" +

        "id=" + getId() +
        ", jvmId=" + getJvmId() +
        ", value='" + getValue() + '\'' +

        '}';
  }
}