package net.faintedge.spiral.networked.sync.control;

import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;

public class ControlSyncCreate extends SynchronizeCreateMessage {

  private short parentSyncObjectId;
  
  public short getParentSyncObjectId() {
    return parentSyncObjectId;
  }

  public void setParentSyncObjectId(short parentSyncObjectId) {
    this.parentSyncObjectId = parentSyncObjectId;
  }
  
}
