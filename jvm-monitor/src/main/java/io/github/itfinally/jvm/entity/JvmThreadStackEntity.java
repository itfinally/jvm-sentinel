package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

@Table( name = "v1_jvm_thread_stack" )
public class JvmThreadStackEntity extends BasicEntity<JvmArgumentsEntity> {

  /**
   * 栈顺序
   */

  private int ordered;

  /**
   * 类名称
   */

  private String className;

  /**
   * 方法名称
   */

  private String methodName;

  /**
   * 文件名
   */

  private String fileName;

  /**
   * 栈行数
   */

  private int lineNumber;

  /**
   * 线程记录 Id
   */

  private long threadInfoId;

  @Column( name = "ordered", nullable = false )
  public int getOrdered() {
    return ordered;
  }

  public JvmThreadStackEntity setOrdered( int ordered ) {
    this.ordered = ordered;
    return this;
  }

  @Column( name = "class_name", nullable = false )
  public String getClassName() {
    return className;
  }

  public JvmThreadStackEntity setClassName( String className ) {
    this.className = className;
    return this;
  }

  @Column( name = "method_name", nullable = false )
  public String getMethodName() {
    return methodName;
  }

  public JvmThreadStackEntity setMethodName( String methodName ) {
    this.methodName = methodName;
    return this;
  }

  @Column( name = "file_name", nullable = false )
  public String getFileName() {
    return fileName;
  }

  public JvmThreadStackEntity setFileName( String fileName ) {
    this.fileName = fileName;
    return this;
  }

  @Column( name = "line_number", nullable = false )
  public int getLineNumber() {
    return lineNumber;
  }

  public JvmThreadStackEntity setLineNumber( int lineNumber ) {
    this.lineNumber = lineNumber;
    return this;
  }

  @Column( name = "thread_info_id", nullable = false )
  public long getThreadInfoId() {
    return threadInfoId;
  }

  public JvmThreadStackEntity setThreadInfoId( long threadInfoId ) {
    this.threadInfoId = threadInfoId;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmThreadStackEntity ) ) return false;
    JvmThreadStackEntity that = ( JvmThreadStackEntity ) o;

    return Objects.equals( getOrdered(), that.getOrdered() ) &&
        Objects.equals( getClassName(), that.getClassName() ) &&
        Objects.equals( getMethodName(), that.getMethodName() ) &&
        Objects.equals( getFileName(), that.getFileName() ) &&
        Objects.equals( getLineNumber(), that.getLineNumber() ) &&
        Objects.equals( getThreadInfoId(), that.getThreadInfoId() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getOrdered(), getClassName(), getMethodName(), getFileName(), getLineNumber(), getThreadInfoId()
    );
  }

  @Override
  public String toString() {
    return "JvmThreadStackEntity{" +

        "ordered=" + getOrdered() +
        ", className='" + getClassName() + '\'' +
        ", methodName='" + getMethodName() + '\'' +
        ", fileName='" + getFileName() + '\'' +
        ", lineNumber=" + getLineNumber() +
        ", threadInfoId=" + getThreadInfoId() +

        '}';
  }
}