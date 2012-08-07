package net.faintedge.spiral.networked.sync.control;

import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;

public class ControlSync extends SynchronizeMessage {

  private short parentSyncObjectId;
  
  public short getParentSyncObjectId() {
    return parentSyncObjectId;
  }

  public void setParentSyncObjectId(short parentSyncObjectId) {
    this.parentSyncObjectId = parentSyncObjectId;
  }
  
}
