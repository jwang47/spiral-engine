package net.faintedge.spiral.networked.msg;

public class SyncCreate<T> {

  private int entityId = -1;
  private short syncObjectId = -1;
  private short syncManagerId = -1;
  private int ownerId = -1;

  public int getEntityId() {
    return entityId;
  }

  public void setEntityId(int entityId) {
    this.entityId = entityId;
  }

  public short getSyncObjectId() {
    return syncObjectId;
  }

  public void setSyncObjectId(short syncObjectId) {
    this.syncObjectId = syncObjectId;
  }

  public short getSyncManagerId() {
    return syncManagerId;
  }

  public void setSyncManagerId(short syncManagerId) {
    this.syncManagerId = syncManagerId;
  }

  public int getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }
  
}
