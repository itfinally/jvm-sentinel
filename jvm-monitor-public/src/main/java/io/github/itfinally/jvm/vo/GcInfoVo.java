package io.github.itfinally.jvm.vo;

import io.github.itfinally.jvm.entity.JvmGcEntity;
import io.github.itfinally.jvm.entity.JvmMemoryEntity;

import java.io.Serializable;
import java.util.List;

public class GcInfoVo implements Serializable {
  private JvmGcEntity jvmGcEntity;

  private List<JvmMemoryEntity> beforeGcMemoryEntities;

  private List<JvmMemoryEntity> afterGcMemoryEntities;

  public GcInfoVo() {
  }

  public GcInfoVo( JvmGcEntity jvmGcEntity, List<JvmMemoryEntity> beforeGcMemoryEntities, List<JvmMemoryEntity> afterGcMemoryEntities ) {
    this.jvmGcEntity = jvmGcEntity;
    this.beforeGcMemoryEntities = beforeGcMemoryEntities;
    this.afterGcMemoryEntities = afterGcMemoryEntities;
  }

  public JvmGcEntity getJvmGcEntity() {
    return jvmGcEntity;
  }

  public GcInfoVo setJvmGcEntity( JvmGcEntity jvmGcEntity ) {
    this.jvmGcEntity = jvmGcEntity;
    return this;
  }

  public List<JvmMemoryEntity> getBeforeGcMemoryEntities() {
    return beforeGcMemoryEntities;
  }

  public GcInfoVo setBeforeGcMemoryEntities( List<JvmMemoryEntity> beforeGcMemoryEntities ) {
    this.beforeGcMemoryEntities = beforeGcMemoryEntities;
    return this;
  }

  public List<JvmMemoryEntity> getAfterGcMemoryEntities() {
    return afterGcMemoryEntities;
  }

  public GcInfoVo setAfterGcMemoryEntities( List<JvmMemoryEntity> afterGcMemoryEntities ) {
    this.afterGcMemoryEntities = afterGcMemoryEntities;
    return this;
  }
}
