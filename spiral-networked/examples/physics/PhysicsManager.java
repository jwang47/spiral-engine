package physics;

import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.networked.PhysicsSpatialSyncCreate;
import net.faintedge.spiral.physics.PhysicsControl;
import net.faintedge.spiral.physics.PhysicsFactory;
import net.faintedge.spiral.physics.PhysicsSystem;
import net.faintedge.spiral.spatial.Node;
import net.faintedge.spiral.spatial.Rectangle;

import org.newdawn.slick.Color;

import com.captiveimagination.jgn.synchronization.SyncObjectManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;


public class PhysicsManager implements SyncObjectManager {
  
  private Node rootNode;
  private PhysicsSystem system;

  public PhysicsManager(Node rootNode, PhysicsSystem system) {
    this.rootNode = rootNode;
    this.system = system;
  }

  @Override
  public Object create(SynchronizeCreateMessage msg) {
    if (msg instanceof PhysicsSpatialSyncCreate) {
      PhysicsSpatialSyncCreate m = (PhysicsSpatialSyncCreate) msg;
      assert m.getSpatial() != null : "msg.spatialCreate was null";
      Spatial spatial = PhysicsSpatialSyncCreate.createPhysicsSpatial(m, false, system, 10.0f);
      rootNode.add(spatial);
      return spatial;
    }
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean remove(SynchronizeRemoveMessage msg, Object object) {
    rootNode.remove((Spatial) object);
    return true;
  }
  
}