package net.faintedge.spiral.networked.msg;

public class SyncUpdate<T> {

  private long timestamp = 0;
  private short syncObjectId = -1;
  private short syncManagerId = -1;
  
  public long getTimestamp() {
    return timestamp;
  }
  public void setTimestamp(long timestamp) {
    this.timestamp = timestamp;
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
  
}
