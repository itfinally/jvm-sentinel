package io.github.itfinally.jvm.vo;

import io.github.itfinally.jvm.entity.JvmArgumentsEntity;
import io.github.itfinally.jvm.entity.JvmStatusEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class JvmRegisterVo implements Serializable {
  private JvmStatusEntity jvmStatusEntity;

  private List<JvmArgumentsEntity> jvmArgumentsEntities;

  public JvmRegisterVo() {
  }

  public JvmRegisterVo( JvmStatusEntity jvmStatusEntity, List<JvmArgumentsEntity> jvmArgumentsEntities ) {
    this.jvmStatusEntity = jvmStatusEntity;
    this.jvmArgumentsEntities = jvmArgumentsEntities;
  }

  public JvmStatusEntity getJvmStatusEntity() {
    return jvmStatusEntity;
  }

  public JvmRegisterVo setJvmStatusEntity( JvmStatusEntity jvmStatusEntity ) {
    this.jvmStatusEntity = jvmStatusEntity;
    return this;
  }

  public List<JvmArgumentsEntity> getJvmArgumentsEntities() {
    return jvmArgumentsEntities;
  }

  public JvmRegisterVo setJvmArgumentsEntities( List<JvmArgumentsEntity> jvmArgumentsEntities ) {
    this.jvmArgumentsEntities = jvmArgumentsEntities;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;
    JvmRegisterVo that = ( JvmRegisterVo ) o;
    return Objects.equals( jvmStatusEntity, that.jvmStatusEntity ) &&
        Objects.equals( jvmArgumentsEntities, that.jvmArgumentsEntities );
  }

  @Override
  public int hashCode() {
    return Objects.hash( jvmStatusEntity, jvmArgumentsEntities );
  }

  @Override
  public String toString() {
    return "JvmRegisterVo{" +
        "jvmStatusEntity=" + jvmStatusEntity +
        ", jvmArgumentsEntities=" + jvmArgumentsEntities +
        '}';
  }
}
