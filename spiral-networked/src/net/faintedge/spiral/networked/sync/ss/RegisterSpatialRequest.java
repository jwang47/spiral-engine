package net.faintedge.spiral.networked.sync.ss;

import net.faintedge.spiral.networked.PhysicsSpatialSyncCreate;

import com.captiveimagination.jgn.message.Message;
import com.captiveimagination.jgn.message.type.PlayerMessage;

public class RegisterSpatialRequest extends Message implements PlayerMessage {
  
  private int requestId = -1;
  private boolean clientControlled = false;
  private PhysicsSpatialSyncCreate create;
  
  public PhysicsSpatialSyncCreate getCreate() {
    return create;
  }

  public void setCreate(PhysicsSpatialSyncCreate create) {
    this.create = create;
  }

  public int getRequestId() {
    return requestId;
  }

  public void setRequestId(int requestId) {
    this.requestId = requestId;
  }

  public boolean isClientControlled() {
    return clientControlled;
  }

  public void setClientControlled(boolean clientControlled) {
    this.clientControlled = clientControlled;
  }
  
}
