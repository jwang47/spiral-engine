package net.faintedge.spiral.networked;

import net.faintedge.spiral.networked.msg.SyncCreate;
import net.faintedge.spiral.networked.msg.SyncDestroy;
import net.faintedge.spiral.networked.msg.SyncUpdate;

public abstract class SyncHandler<T> {

  public abstract SyncCreate<T> makeCreateMessage(T object);
  public abstract SyncUpdate<T> makeUpdateMessage(T object);
  public abstract SyncDestroy<T> makeDestroyMessage(T object);
  
  public abstract T create(SyncCreate<T> message);
  public abstract void destroy(T object);
  public abstract void applyUpdate(T object, SyncUpdate<T> message);

}
