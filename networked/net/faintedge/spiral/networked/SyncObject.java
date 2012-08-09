package net.faintedge.spiral.networked;

import net.faintedge.spiral.networked.msg.SyncUpdate;
import util.Log;

import com.artemis.Component;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

public class SyncObject<T> extends Component {

  private T object;
  private short syncObjectId = -1;
  private short syncManagerId = -1;
  private long requestId = -1;
  private int ownerId = -1;
  
  private int updateDelay = 20;
  private int timeSinceLastUpdate = updateDelay;

  public T getObject() {
    return object;
  }

  public void setObject(T object) {
    this.object = object;
  }

  public short getSyncObjectId() {
    return syncObjectId;
  }

  public void setSyncObjectId(short syncObjectId) {
    this.syncObjectId = syncObjectId;
  }

  public short getSyncManagerId() {
    return syncManagerId;
  }

  public void setSyncManagerId(short syncManagerId) {
    this.syncManagerId = syncManagerId;
  }

  public long getRequestId() {
    return requestId;
  }

  public void setRequestId(long requestId) {
    this.requestId = requestId;
  }

  public int getOwnerId() {
    return ownerId;
  }

  public void setOwnerId(int ownerId) {
    this.ownerId = ownerId;
  }

  public void update(Server server, SyncHandler<T> handler, int delta) {
    timeSinceLastUpdate += delta;
    if (timeSinceLastUpdate > updateDelay) {
      timeSinceLastUpdate = 0;
      SyncUpdate<T> update = handler.makeUpdateMessage(object);
      update.setSyncManagerId(syncManagerId);
      update.setSyncObjectId(syncObjectId);
      if (ownerId == -1) {
        // it's server authoritative (ours)
        server.sendToAllUDP(update);
        Log.info("server sending update for its own box");
      } else {
        // don't send it back to the owner
        server.sendToAllExceptUDP(ownerId, update);
      }
    }
  }

  public void update(Client client, SyncHandler<T> handler, int delta) {
    timeSinceLastUpdate += delta;
    if (timeSinceLastUpdate > updateDelay) {
      timeSinceLastUpdate = 0;
      SyncUpdate<T> update = handler.makeUpdateMessage(object);
      update.setSyncManagerId(syncManagerId);
      update.setSyncObjectId(syncObjectId);
      client.sendUDP(update);
      Log.info("client sending sync update");
    }
  }
  
}
