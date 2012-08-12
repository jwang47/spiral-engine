package net.faintedge.spiral.networked.sync.msg;

public class SyncCreate<T> {

  private int entityId = -1;
  private short syncObjectId = -1;
  private short syncManagerId = -1;
  private int ownerId = -1;
  /**
   * True if we register from client and we want the server to replicate it to the other clients.
   * Useful when we just want client to synchronize its input state with the server.
   */
  private boolean replicate = true;

  public boolean isReplicated() {
    return replicate;
  }

  public void setReplicate(boolean replicate) {
    this.replicate = replicate;
  }

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
