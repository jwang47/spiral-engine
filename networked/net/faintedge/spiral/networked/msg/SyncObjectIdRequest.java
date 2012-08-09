package net.faintedge.spiral.networked.msg;

public class SyncObjectIdRequest {

  public static final short REQUEST = 0;
  public static final short REPLY = 1;
  
  private short phase = REQUEST;
  private short syncObjectId = -1;
  private short requestId = -1;

  public short getRequestId() {
    return requestId;
  }

  public void setRequestId(short requestId) {
    this.requestId = requestId;
  }

  public short getPhase() {
    return phase;
  }

  public void setPhase(short phase) {
    this.phase = phase;
  }

  public short getSyncObjectId() {
    return syncObjectId;
  }

  public void setSyncObjectId(short syncObjectId) {
    this.syncObjectId = syncObjectId;
  }
  
}
