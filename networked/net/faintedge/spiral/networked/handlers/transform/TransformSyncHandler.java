package net.faintedge.spiral.networked.handlers.transform;

import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.networked.SyncHandler;
import net.faintedge.spiral.networked.msg.SyncCreate;
import net.faintedge.spiral.networked.msg.SyncDestroy;
import net.faintedge.spiral.networked.msg.SyncUpdate;
import util.Log;

public class TransformSyncHandler extends SyncHandler<Transform> {
  
  @Override
  public SyncCreate<Transform> makeCreateMessage(Transform object) {
    TransformSyncCreate create = new TransformSyncCreate();
    return create;
  }

  @Override
  public SyncUpdate<Transform> makeUpdateMessage(Transform object) {
    TransformSyncUpdate update = new TransformSyncUpdate();
    update.setX(object.getTranslation().getX());
    update.setY(object.getTranslation().getY());
    update.setRotation(object.getRotation());
    return update;
  }

  @Override
  public SyncDestroy<Transform> makeDestroyMessage(Transform object) {
    return new TransformSyncDestroy();
  }

  @Override
  public Transform create(SyncCreate<Transform> message) {
//    TransformSyncCreate create = (TransformSyncCreate) message;
    Transform transform = new Transform();
    return transform;
  }

  @Override
  public void applyUpdate(Transform object, SyncUpdate<Transform> message) {
    TransformSyncUpdate update = (TransformSyncUpdate) message;
    object.getTranslation().set(update.getX(), update.getY());
    object.setRotation(update.getRotation());
  }

}
