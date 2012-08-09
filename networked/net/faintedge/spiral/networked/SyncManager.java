package net.faintedge.spiral.networked;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.faintedge.spiral.networked.msg.SyncCreate;
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
  
  private SyncHandler<T> handler;
  private short syncManagerId;
  
  // shared
  private Map<Short, SyncObject<T>> syncObjects = new HashMap<Short, SyncObject<T>>();
  private Bag<SyncObject<T>> mySyncObjects = new Bag<SyncObject<T>>();
  
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
  
  public SyncManager(SyncHandler<T> handler, short syncManagerId) {
    this.handler = handler;
    this.syncManagerId = syncManagerId;
  }
  
  public void setServer(Server server) {
    this.server = server;
    server.addListener(this);
  }
  
  public void setClient(Client client) {
    this.client = client;
    client.addListener(this);
  }

  protected void processEntities(ImmutableBag<Entity> entities, int delta) {
    if (server != null) {
      // send updates for all stuff
      for (SyncObject<T> syncObject : syncObjects.values()) {
        syncObject.update(server, handler, delta);
      }
    } else if (client != null) {
      // send updates for stuff we're authoritative over
      for (int i = 0, s = mySyncObjects.size(); i < s; i++) {
        mySyncObjects.get(i).update(client, handler, delta);
      }
    }
    
    // apply updates for stuff we're not
    while (!pendingUpdates.isEmpty()) {
      SyncUpdate<T> update = pendingUpdates.poll();
      SyncObject<T> syncObject = syncObjects.get(update.getSyncObjectId());
      if (syncObject == null || syncObject.getObject() == null) {
        Log.warn("syncObject or syncObject.object was null for an update");
        continue;
      }
      handler.applyUpdate(syncObject.getObject(), update);
    }
  }
  
  public void added(T object, SyncObject<T> syncObject) {
    syncObject.setObject(object);
    syncObject.setSyncManagerId(syncManagerId);
    if (server != null) {
      short syncObjectId = nextSyncObjectId++;
      SyncCreate<T> create = handler.makeCreateMessage(syncObject.getObject());
      create.setSyncObjectId(syncObjectId);
      create.setOwnerId(-2);
      syncObject.setSyncObjectId(syncObjectId);
      syncObject.setOwnerId(-2);
      syncObjects.put(syncObjectId, syncObject);
      mySyncObjects.add(syncObject);
      server.sendToAllTCP(create);
    } else if (client != null) {
      // we're client, so we need to get a sync object id from server
      SyncObjectIdRequest request = new SyncObjectIdRequest();
      short requestId = currRequestId++;
      syncObject.setRequestId(requestId);
      request.setRequestId(requestId);
      needsReply.put(requestId, syncObject);
      pendingIdRequests.add(request);
      client.sendTCP(request);
    }
  }

  public void removed(T object, SyncObject<T> syncObject) {
    // TODO: unregister
  }

  @Override
  public void connected(Connection connection) {
    if (server != null) {
      // send new client create messages
      Log.info("sending client sync create messages");
      for (SyncObject<T> syncObject : syncObjects.values()) {
        SyncCreate<T> create = handler.makeCreateMessage(syncObject.getObject());
        Log.info("sending create for obj id " + syncObject.getSyncObjectId());
        create.setOwnerId(syncObject.getOwnerId());
        create.setSyncManagerId(syncObject.getSyncManagerId());
        create.setSyncObjectId(syncObject.getSyncObjectId());
        connection.sendTCP(create);
      }
    }
  }

  @Override
  public void disconnected(Connection connection) {
    if (server != null) {
      // send destroy messages for the connection's sync objects
    }
  }

  @Override
  public void received(Connection connection, Object message) {
    if (message instanceof SyncUpdate) {
      SyncUpdate<T> update = (SyncUpdate<T>) message;
      if (update.getSyncManagerId() == syncManagerId) {
        // got a sync update for me!
        Assert.isTrue(update.getSyncObjectId() != -1);
        pendingUpdates.add(update);
      }
    } else if (message instanceof SyncCreate) {
      SyncCreate<T> create = (SyncCreate<T>) message;
      Assert.isTrue(create.getSyncManagerId() == syncManagerId);
      Assert.isTrue(create.getSyncObjectId() != -1);
      Log.info("received sync create for object id " + create.getSyncObjectId());
      // create a replicated sync object
      T object = handler.create(create);
      SyncObject<T> syncObject = new SyncObject<T>();
      syncObject.setObject(object);
      if (create.getOwnerId() == -1) {
        syncObject.setOwnerId(connection.getID());
      }
      syncObject.setSyncObjectId(create.getSyncObjectId());
      syncObject.setSyncManagerId(syncManagerId);
      syncObjects.put(create.getSyncObjectId(), syncObject);
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
        Assert.isTrue(syncObject != null, "got a SyncObjectIdRequest reply, but no syncObject for requestId " + requestId);
        syncObject.setSyncObjectId(syncObjectId);
        syncObject.setSyncManagerId(syncManagerId);
        Log.info("client object was assigned sync object id " + idRequest.getSyncObjectId());
        // now the client can register
        SyncCreate<T> create = handler.makeCreateMessage(syncObject.getObject());
        create.setSyncObjectId(syncObjectId);
        create.setSyncManagerId(syncManagerId);
        syncObjects.put(syncObjectId, syncObject);
        mySyncObjects.add(syncObject);
        connection.sendTCP(create);
      }
    }
  }

  @Override
  public void idle(Connection connection) {
    
  }
}
