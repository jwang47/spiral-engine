package simple;

import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.spatial.Node;
import net.faintedge.spiral.spatial.Rectangle;

import org.newdawn.slick.Color;

import com.captiveimagination.jgn.synchronization.SyncObjectManager;
import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;
import com.captiveimagination.jgn.synchronization.message.SynchronizeRemoveMessage;


public class SimpleManager implements SyncObjectManager {
  
  private Node rootNode;

  public SimpleManager(Node rootNode) {
    this.rootNode = rootNode;
  }

  @Override
  public Object create(SynchronizeCreateMessage msg) {
    Spatial remote = new Rectangle(Color.green, 20, 20);
    rootNode.add(remote);
    return remote;
  }

  @Override
  public boolean remove(SynchronizeRemoveMessage msg, Object object) {
    rootNode.remove((Spatial) object);
    return true;
  }
  
}