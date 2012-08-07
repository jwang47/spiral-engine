package net.faintedge.spiral.networked;

import net.faintedge.spiral.core.Spatial;

import com.captiveimagination.jgn.synchronization.GraphicalController;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;

public class SpatialGraphicalController implements GraphicalController<Spatial> {

  @Override
  public void applySynchronizationMessage(SynchronizeMessage msg, Spatial spatial) {
    SpatialSync sync = (SpatialSync) msg;
    spatial.getTranslation().set(sync.getX(), sync.getY());
    spatial.setRotation(sync.getRotation());
  }

  @Override
  public SynchronizeMessage createSynchronizationMessage(Spatial spatial) {
    SpatialSync sync = new SpatialSync();
    sync.setX(spatial.getTranslation().getX());
    sync.setY(spatial.getTranslation().getY());
    sync.setRotation(spatial.getRotation());
    return sync;
  }

  @Override
  public float proximity(Spatial spatial, short playerId) {
    return 1.0f;
  }

  @Override
  public boolean validateCreate(SynchronizeCreateMessage msg) {
    return true;
  }

  @Override
  public boolean validateMessage(SynchronizeMessage msg, Spatial arg1) {
    return true;
  }

  @Override
  public boolean validateRemove(SynchronizeRemoveMessage msg) {
    return true;
  }

}
