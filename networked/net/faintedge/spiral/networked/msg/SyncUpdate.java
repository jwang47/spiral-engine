package net.faintedge.spiral.networked.msg;

public class SyncUpdate<T> {

  private short syncObjectId = -1;
  private short syncManagerId = -1;
  
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
  
}
