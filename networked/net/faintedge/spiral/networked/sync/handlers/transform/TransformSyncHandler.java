package net.faintedge.spiral.networked.sync.handlers.transform;

import com.esotericsoftware.kryo.Kryo;

import net.faintedge.spiral.core.component.Transform;
import net.faintedge.spiral.networked.sync.SyncHandler;
import net.faintedge.spiral.networked.sync.handlers.physics.PhysicsSyncCreate;
import net.faintedge.spiral.networked.sync.handlers.physics.PhysicsSyncDestroy;
import net.faintedge.spiral.networked.sync.handlers.physics.PhysicsSyncUpdate;
import net.faintedge.spiral.networked.sync.msg.SyncCreate;
import net.faintedge.spiral.networked.sync.msg.SyncDestroy;
import net.faintedge.spiral.networked.sync.msg.SyncUpdate;
import util.Log;

public class TransformSyncHandler extends SyncHandler<Transform> {

  public TransformSyncHandler(Kryo kryo) {
    super(kryo);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void registerMessages(Kryo kryo) {
    kryo.register(TransformSyncCreate.class);
    kryo.register(TransformSyncUpdate.class);
    kryo.register(TransformSyncDestroy.class);
  }
  
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
    return new Transform();
  }

  @Override
  public void destroy(Transform object) {
    
  }

  @Override
  public void applyUpdate(Transform object, SyncUpdate<Transform> message) {
    TransformSyncUpdate update = (TransformSyncUpdate) message;
    object.getTranslation().set(update.getX(), update.getY());
    object.setRotation(update.getRotation());
  }

}
