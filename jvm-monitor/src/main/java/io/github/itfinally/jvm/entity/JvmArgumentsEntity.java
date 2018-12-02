package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

@Table( name = "v1_jvm_arguments" )
public class JvmArgumentsEntity extends BasicEntity<JvmArgumentsEntity> {

  /**
   * 虚拟机启动记录Id
   */
  
  private String statusId;

  /**
   * 虚拟机启动记录Id
   */
  
  private String jvmStatusId;

  /**
   * 虚拟机启动时使用的参数
   */
  
  private String value;

  @Column( name = "status_id", nullable = false )
  public String getStatusId() {
    return statusId;
  }

  public JvmArgumentsEntity setStatusId( String statusId ) {
    this.statusId = statusId;
    return this;
  }

  @Column( name = "jvm_status_id", nullable = false )
  public String getJvmStatusId() {
    return jvmStatusId;
  }

  public JvmArgumentsEntity setJvmStatusId( String jvmStatusId ) {
    this.jvmStatusId = jvmStatusId;
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
    
    return Objects.equals( getStatusId(), that.getStatusId() ) &&
            Objects.equals( getJvmStatusId(), that.getJvmStatusId() ) &&
            Objects.equals( getValue(), that.getValue() ) ;
    
  }

  @Override
  public int hashCode() {
    return Objects.hash( getStatusId(), getJvmStatusId(), getValue() );
  }

  @Override
  public String toString() {
    return "JvmArgumentsEntity{" +
            
            "statusId='" + getStatusId() + '\'' +
            ", jvmStatusId='" + getJvmStatusId() + '\'' +
            ", value='" + getValue() + '\'' +
            
            '}';
    }
}