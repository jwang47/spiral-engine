package net.faintedge.spiral.networked;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.faintedge.spiral.networked.msg.SyncCreate;
import net.faintedge.spiral.networked.msg.SyncDestroy;
import net.faintedge.spiral.networked.msg.SyncObjectIdRequest;
import net.faintedge.spiral.networked.msg.SyncUpdate;
import util.Assert;
import util.Log;

import com.artemis.Component;
import com.artemis.Entity;
import com.artemis.utils.Bag;
import com.artemis.utils.ImmutableBag;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class SyncManager<T extends Component> extends Listener {
  
  private static int SERVER_OWNER_ID = -2;
  
  private SyncHandler<T> handler;
  private short syncManagerId;
  
  // shared
  private Map<Short, SyncObject<T>> syncObjectsById = new HashMap<Short, SyncObject<T>>();
  private Map<Integer, Bag<SyncObject<T>>> syncObjectsByOwner = new HashMap<Integer, Bag<SyncObject<T>>>();
  
  // server stuff
  private Server server;
  private short nextSyncObjectId = 0;
  
  // client stuff
  private Client client;
  private short currRequestId = 0;
  private Map<Short, SyncObject<T>> needsReply = new HashMap<Short, SyncObject<T>>();
  private Queue<SyncUpdate<T>> pendingUpdates = new ConcurrentLinkedQueue<SyncUpdate<T>>();
  private Queue<SyncObject<T>> pendingAdds = new ConcurrentLinkedQueue<SyncObject<T>>();
  private Queue<SyncObjectIdRequest> pendingIdRequests = new ConcurrentLinkedQueue<SyncObjectIdRequest>();

  private int myOwnerId = -1;
  
  public SyncManager(SyncHandler<T> handler, short syncManagerId) {
    this.handler = handler;
    this.syncManagerId = syncManagerId;
  }
  
  public void setServer(Server server) {
    this.server = server;
    server.addListener(this);
    myOwnerId = SERVER_OWNER_ID;
    syncObjectsByOwner.put(SERVER_OWNER_ID, new Bag<SyncObject<T>>());
  }
  
  public void setClient(Client client) {
    this.client = client;
    client.addListener(this);
  }
  
  private Bag<SyncObject<T>> getMySyncObjects() {
    return syncObjectsByOwner.get(myOwnerId);
  }

  protected void processEntities(ImmutableBag<Entity> entities, int delta) {
    if (server != null) {
      // send updates for all stuff
      for (SyncObject<T> syncObject : syncObjectsById.values()) {
        syncObject.update(server, handler, delta);
      }
    } else if (client != null) {
      // send updates for stuff we're authoritative over
      Bag<SyncObject<T>> mySyncObjects = getMySyncObjects();
      for (int i = 0, s = mySyncObjects.size(); i < s; i++) {
        mySyncObjects.get(i).update(client, handler, delta);
      }
    }
    
    // apply updates for stuff we're not
    while (!pendingUpdates.isEmpty()) {
      SyncUpdate<T> update = pendingUpdates.poll();
      SyncObject<T> syncObject = syncObjectsById.get(update.getSyncObjectId());
      if (syncObject == null || syncObject.getObject() == null) {
        Log.warn("syncObject or syncObject.object was null for an update");
        continue;
      }
      syncObject.applyUpdate(handler, update);
    }
  }
  
  private void addSyncObject(T object, SyncObject<T> syncObject, SyncCreate<T> create) {
    Assert.isTrue(object != null);
    Assert.isTrue(create.getOwnerId() != -1);
    Assert.isTrue(create.getSyncManagerId() != -1);
    Assert.isTrue(create.getSyncObjectId() != -1);
    
    syncObject.setObject(object);
    syncObject.setOwnerId(create.getOwnerId());
    syncObject.setSyncManagerId(create.getSyncManagerId());
    syncObject.setSyncObjectId(create.getSyncObjectId());
    
    syncObjectsById.put(create.getSyncObjectId(), syncObject);
    syncObjectsByOwner.get(create.getOwnerId()).add(syncObject);
  }
  
  private void removeSyncObject(SyncObject<T> syncObject) {
    Assert.isTrue(syncObject.getObject() != null);
    Assert.isTrue(syncObject.getOwnerId() != -1);
    Assert.isTrue(syncObject.getSyncManagerId() != -1);
    Assert.isTrue(syncObject.getSyncObjectId() != -1);
    
    syncObjectsById.remove(syncObject.getSyncObjectId());
    syncObjectsByOwner.get(syncObject.getOwnerId()).remove(syncObject);
  }

  public void added(T object, SyncObject<T> syncObject) {
    if (server != null) {
      short syncObjectId = nextSyncObjectId++;
      SyncCreate<T> create = handler.makeCreateMessage(syncObject.getObject());
      create.setOwnerId(myOwnerId);
      create.setSyncManagerId(syncManagerId);
      create.setSyncObjectId(syncObjectId);
      addSyncObject(object, syncObject, create);
      server.sendToAllTCP(create);
    } else if (client != null) {
      // we're client, so we need to get a sync object id from server
      SyncObjectIdRequest request = new SyncObjectIdRequest();
      short requestId = currRequestId++;
      syncObject.setObject(object);
      syncObject.setRequestId(requestId);
      syncObject.setSyncManagerId(syncManagerId);
      request.setRequestId(requestId);
      needsReply.put(requestId, syncObject);
      pendingIdRequests.add(request);
      client.sendTCP(request);
    }
  }

  public void removed(T object, SyncObject<T> syncObject) {
    // unregister
    SyncDestroy<T> destroy = handler.makeDestroyMessage(object);
    destroy.setOwnerId(syncObject.getOwnerId());
    destroy.setSyncManagerId(syncManagerId);
    destroy.setSyncObjectId(syncObject.getSyncObjectId());

    // remove locally if it's ours
    if (destroy.getOwnerId() == myOwnerId) {
      removeSyncObject(syncObject);
    }
    
    // notify remote end
    if (server != null) {
      server.sendToAllTCP(destroy);
    } else if (client != null) {
      client.sendTCP(destroy);
    }
  }

  @Override
  public void connected(Connection connection) {
    if (server != null) {
      // send new client create messages
      syncObjectsByOwner.put(connection.getID(), new Bag<SyncObject<T>>());
      for (SyncObject<T> syncObject : syncObjectsById.values()) {
        SyncCreate<T> create = handler.makeCreateMessage(syncObject.getObject());
        Log.info("sending create for obj id " + syncObject.getSyncObjectId());
        create.setOwnerId(syncObject.getOwnerId());
        create.setSyncManagerId(syncObject.getSyncManagerId());
        create.setSyncObjectId(syncObject.getSyncObjectId());
        connection.sendTCP(create);
      }
    } else if (client != null) {
      myOwnerId = client.getID();
      syncObjectsByOwner.put(SERVER_OWNER_ID, new Bag<SyncObject<T>>());
      syncObjectsByOwner.put(myOwnerId, new Bag<SyncObject<T>>());
    }
  }

  @Override
  public void disconnected(Connection connection) {
    if (server != null) {
      // send destroy messages for the connection's sync objects
      // also remove the syncObjects bag for that owner
      Bag<SyncObject<T>> syncObjects = syncObjectsByOwner.remove(connection.getID());
      for (int i = 0; i < syncObjects.size(); i++) {
        SyncObject<T> syncObject = syncObjects.get(i);
        SyncCreate<T> create = handler.makeCreateMessage(syncObject.getObject());
        Log.info("sending create for obj id " + syncObject.getSyncObjectId());
        create.setOwnerId(syncObject.getOwnerId());
        create.setSyncManagerId(syncObject.getSyncManagerId());
        create.setSyncObjectId(syncObject.getSyncObjectId());
        connection.sendTCP(create);
      }
    } else if (client != null) {
      // server died?
    }
  }

  @Override
  public void received(Connection connection, Object message) {
    if (message instanceof SyncUpdate) {
      SyncUpdate<T> update = (SyncUpdate<T>) message;
      // got a sync update for me?
      if (update.getSyncManagerId() == syncManagerId) {
        Assert.isTrue(update.getSyncObjectId() != -1);
        pendingUpdates.add(update);
      }
    } else if (message instanceof SyncCreate) {
      // create a replicated sync object
      SyncCreate<T> create = (SyncCreate<T>) message;
      Log.info("received sync create for object id " + create.getSyncObjectId());
      addSyncObject(handler.create(create), new SyncObject<T>(), create);
    } else if (message instanceof SyncObjectIdRequest) {
      SyncObjectIdRequest idRequest = (SyncObjectIdRequest) message;
      if (idRequest.getPhase() == SyncObjectIdRequest.REQUEST) {
        idRequest.setPhase(SyncObjectIdRequest.REPLY);
        idRequest.setSyncObjectId(nextSyncObjectId++);
        connection.sendTCP(idRequest);
        Log.info("server provided sync object id " + idRequest.getSyncObjectId());
      } else if (idRequest.getPhase() == SyncObjectIdRequest.REPLY) {
        short requestId = idRequest.getRequestId();
        short syncObjectId = idRequest.getSyncObjectId();
        SyncObject<T> syncObject = needsReply.get(requestId);
        T object = syncObject.getObject();
        Assert.isTrue(syncObject != null, "got a SyncObjectIdRequest reply, but no syncObject for requestId " + requestId);
        syncObject.setOwnerId(myOwnerId);
        syncObject.setSyncManagerId(syncManagerId);
        syncObject.setSyncObjectId(syncObjectId);
        Log.info("client object was assigned sync object id " + idRequest.getSyncObjectId());
        
        // now the client can register
        SyncCreate<T> create = handler.makeCreateMessage(syncObject.getObject());
        create.setOwnerId(myOwnerId);
        create.setSyncManagerId(syncManagerId);
        create.setSyncObjectId(syncObjectId);
        addSyncObject(object, syncObject, create);
        connection.sendTCP(create);
      }
    }
  }

  @Override
  public void idle(Connection connection) {
    
  }
}
