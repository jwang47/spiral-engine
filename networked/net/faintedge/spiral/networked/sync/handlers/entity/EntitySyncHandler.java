package net.faintedge.spiral.networked.sync.handlers.entity;

import com.esotericsoftware.kryo.Kryo;

import net.faintedge.spiral.networked.sync.SyncHandler;
import net.faintedge.spiral.networked.sync.handlers.transform.TransformSyncCreate;
import net.faintedge.spiral.networked.sync.handlers.transform.TransformSyncDestroy;
import net.faintedge.spiral.networked.sync.handlers.transform.TransformSyncUpdate;
import net.faintedge.spiral.networked.sync.msg.SyncCreate;
import net.faintedge.spiral.networked.sync.msg.SyncDestroy;
import net.faintedge.spiral.networked.sync.msg.SyncUpdate;

public class EntitySyncHandler extends SyncHandler<EntitySync> {

  public EntitySyncHandler(Kryo kryo) {
    super(kryo);
    // TODO Auto-generated constructor stub
  }

  @Override
  public void registerMessages(Kryo kryo) {
    kryo.register(EntitySyncCreate.class);
    kryo.register(EntitySyncDestroy.class);
  }

  @Override
  public SyncCreate<EntitySync> makeCreateMessage(EntitySync object) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SyncUpdate<EntitySync> makeUpdateMessage(EntitySync object) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public SyncDestroy<EntitySync> makeDestroyMessage(EntitySync object) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public EntitySync create(SyncCreate<EntitySync> message) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public void destroy(EntitySync object) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public void applyUpdate(EntitySync object, SyncUpdate<EntitySync> message) {
    // TODO Auto-generated method stub
    
  }

}
