package io.github.itfinally.jvm.entity;

import java.util.Objects;
import javax.persistence.*;

import java.util.Date;

@Table( name = "v1_jvm_memory" )
public class JvmMemoryEntity extends BasicEntity<JvmMemoryEntity> {

  /**
   * 内存快照时间
   */

  private Date createTime = new Date();

  /**
   * 内存空间名称
   */

  private String memorySpaceName;

  /**
   * 内存起始值
   */

  private long init;

  /**
   * 内存使用量
   */

  private long used;

  /**
   * 内存动态阀值
   */

  private long committed;

  /**
   * 内存最大阀值
   */

  private long max;

  /**
   * 虚拟机Id
   */

  private long jvmId;

  /**
   * True 即代表 gc 前后的快照, 否则为普通内存快照
   */

  private boolean snapshotForGc;

  /**
   * True 即代表发生在 gc 前, 否则为 gc 后
   */

  private boolean happenBeforeGc;

  /**
   * gc Id
   */

  private long jvmGcId;

  @Column( name = "create_time", nullable = false )
  public Date getCreateTime() {
    return createTime;
  }

  public JvmMemoryEntity setCreateTime( Date createTime ) {
    this.createTime = createTime;
    return this;
  }

  @Column( name = "memory_space_name", nullable = false )
  public String getMemorySpaceName() {
    return memorySpaceName;
  }

  public JvmMemoryEntity setMemorySpaceName( String memorySpaceName ) {
    this.memorySpaceName = memorySpaceName;
    return this;
  }

  @Column( name = "init", nullable = false )
  public long getInit() {
    return init;
  }

  public JvmMemoryEntity setInit( long init ) {
    this.init = init;
    return this;
  }

  @Column( name = "used", nullable = false )
  public long getUsed() {
    return used;
  }

  public JvmMemoryEntity setUsed( long used ) {
    this.used = used;
    return this;
  }

  @Column( name = "committed", nullable = false )
  public long getCommitted() {
    return committed;
  }

  public JvmMemoryEntity setCommitted( long committed ) {
    this.committed = committed;
    return this;
  }

  @Column( name = "max", nullable = false )
  public long getMax() {
    return max;
  }

  public JvmMemoryEntity setMax( long max ) {
    this.max = max;
    return this;
  }

  @Column( name = "jvm_id", nullable = false )
  public long getJvmId() {
    return jvmId;
  }

  public JvmMemoryEntity setJvmId( long jvmId ) {
    this.jvmId = jvmId;
    return this;
  }

  @Column( name = "is_snapshot_for_gc", nullable = false )
  public boolean isSnapshotForGc() {
    return snapshotForGc;
  }

  public JvmMemoryEntity setSnapshotForGc( boolean snapshotForGc ) {
    this.snapshotForGc = snapshotForGc;
    return this;
  }

  @Column( name = "is_happen_before_gc", nullable = false )
  public boolean isHappenBeforeGc() {
    return happenBeforeGc;
  }

  public JvmMemoryEntity setHappenBeforeGc( boolean happenBeforeGc ) {
    this.happenBeforeGc = happenBeforeGc;
    return this;
  }

  @Column( name = "jvm_gc_id", nullable = false )
  public long getJvmGcId() {
    return jvmGcId;
  }

  public JvmMemoryEntity setJvmGcId( long jvmGcId ) {
    this.jvmGcId = jvmGcId;
    return this;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof JvmMemoryEntity ) ) return false;
    JvmMemoryEntity that = ( JvmMemoryEntity ) o;

    return Objects.equals( getId(), that.getId() ) &&
        Objects.equals( getCreateTime(), that.getCreateTime() ) &&
        Objects.equals( getMemorySpaceName(), that.getMemorySpaceName() ) &&
        Objects.equals( getInit(), that.getInit() ) &&
        Objects.equals( getUsed(), that.getUsed() ) &&
        Objects.equals( getCommitted(), that.getCommitted() ) &&
        Objects.equals( getMax(), that.getMax() ) &&
        Objects.equals( getJvmId(), that.getJvmId() ) &&
        Objects.equals( isSnapshotForGc(), that.isSnapshotForGc() ) &&
        Objects.equals( isHappenBeforeGc(), that.isHappenBeforeGc() ) &&
        Objects.equals( getJvmGcId(), that.getJvmGcId() );

  }

  @Override
  public int hashCode() {
    return Objects.hash( getId(), getCreateTime(), getMemorySpaceName(), getInit(), getUsed(), getCommitted(),
        getMax(), getJvmId(), isSnapshotForGc(), isHappenBeforeGc(), getJvmGcId() );
  }

  @Override
  public String toString() {
    return "JvmMemoryEntity{" +

        "id=" + getId() +
        ", createTime=" + getCreateTime() +
        ", memorySpaceName='" + getMemorySpaceName() + '\'' +
        ", init=" + getInit() +
        ", used=" + getUsed() +
        ", committed=" + getCommitted() +
        ", max=" + getMax() +
        ", jvmId=" + getJvmId() +
        ", snapshotForGc=" + isSnapshotForGc() +
        ", happenBeforeGc=" + isHappenBeforeGc() +
        ", jvmGcId=" + getJvmGcId() +

        '}';
  }
}