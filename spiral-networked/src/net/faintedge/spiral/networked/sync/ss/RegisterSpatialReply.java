package net.faintedge.spiral.networked.sync.ss;

import net.faintedge.spiral.networked.sync.control.PhysicsMoveSyncCreate;

import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.message.type.PlayerMessage;

public class RegisterSpatialReply extends Message implements PlayerMessage {

  private int requestId;
  private PhysicsMoveSyncCreate moveCreate;
  private short syncObjectId;
  
  public PhysicsMoveSyncCreate getMoveCreate() {
    return moveCreate;
  }

  public void setMoveCreate(PhysicsMoveSyncCreate create) {
    this.moveCreate = create;
  }

  public int getRequestId() {
    return requestId;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  public void setSyncObjectId(short syncObjectId) {
    this.syncObjectId = syncObjectId;
  }

  public short getSyncObjectId() {
    return syncObjectId;
  }
  
}
