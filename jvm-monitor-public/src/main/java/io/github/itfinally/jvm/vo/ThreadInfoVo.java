package io.github.itfinally.jvm.vo;

import io.github.itfinally.jvm.entity.JvmThreadEntity;
import io.github.itfinally.jvm.entity.JvmThreadInfoEntity;
import io.github.itfinally.jvm.entity.JvmThreadStackEntity;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class ThreadInfoVo implements Serializable {
  private JvmThreadEntity jvmThreadEntity;
  private List<JvmThreadInfoEntity> jvmThreadInfoEntities;
  private List<JvmThreadStackEntity> jvmThreadStackEntities;

  public ThreadInfoVo() {
  }

  public ThreadInfoVo( JvmThreadEntity jvmThreadEntity, List<JvmThreadInfoEntity> jvmThreadInfoEntities, List<JvmThreadStackEntity> jvmThreadStackEntities ) {
    this.jvmThreadEntity = jvmThreadEntity;
    this.jvmThreadInfoEntities = jvmThreadInfoEntities;
    this.jvmThreadStackEntities = jvmThreadStackEntities;
  }

  public JvmThreadEntity getJvmThreadEntity() {
    return jvmThreadEntity;
  }

  public ThreadInfoVo setJvmThreadEntity( JvmThreadEntity jvmThreadEntity ) {
    this.jvmThreadEntity = jvmThreadEntity;
    return this;
  }

  public List<JvmThreadInfoEntity> getJvmThreadInfoEntities() {
    return jvmThreadInfoEntities;
  }

  public ThreadInfoVo setJvmThreadInfoEntities( List<JvmThreadInfoEntity> jvmThreadInfoEntities ) {
    this.jvmThreadInfoEntities = jvmThreadInfoEntities;
    return this;
  }

  public List<JvmThreadStackEntity> getJvmThreadStackEntities() {
    return jvmThreadStackEntities;
  }

  public ThreadInfoVo setJvmThreadStackEntities( List<JvmThreadStackEntity> jvmThreadStackEntities ) {
    this.jvmThreadStackEntities = jvmThreadStackEntities;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;
    ThreadInfoVo that = ( ThreadInfoVo ) o;
    return Objects.equals( jvmThreadEntity, that.jvmThreadEntity ) &&
        Objects.equals( jvmThreadInfoEntities, that.jvmThreadInfoEntities ) &&
        Objects.equals( jvmThreadStackEntities, that.jvmThreadStackEntities );
  }

  @Override
  public int hashCode() {
    return Objects.hash( jvmThreadEntity, jvmThreadInfoEntities, jvmThreadStackEntities );
  }

  @Override
  public String toString() {
    return "ThreadInfoVo{" +
        "jvmThreadEntity=" + jvmThreadEntity +
        ", jvmThreadInfoEntities=" + jvmThreadInfoEntities +
        ", jvmThreadStackEntities=" + jvmThreadStackEntities +
        '}';
  }
}
