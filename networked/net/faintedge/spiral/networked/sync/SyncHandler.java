package net.faintedge.spiral.networked.sync;

import net.faintedge.spiral.networked.sync.msg.SyncCreate;
import net.faintedge.spiral.networked.sync.msg.SyncDestroy;
import net.faintedge.spiral.networked.sync.msg.SyncUpdate;

import com.esotericsoftware.kryo.Kryo;

public abstract class SyncHandler<T> {
  
  public SyncHandler(Kryo kryo) {
    this.registerMessages(kryo);
  }

  public abstract void registerMessages(Kryo kryo);
  public abstract SyncCreate<T> makeCreateMessage(T object);
  public abstract SyncUpdate<T> makeUpdateMessage(T object);
  public abstract SyncDestroy<T> makeDestroyMessage(T object);
  
  public abstract T create(SyncCreate<T> message);
  public abstract void destroy(T object);
  public abstract void applyUpdate(T object, SyncUpdate<T> message);

}
