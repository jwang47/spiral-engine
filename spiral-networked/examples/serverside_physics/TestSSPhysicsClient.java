package serverside_physics;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import net.faintedge.spiral.networked.Net;
import net.faintedge.spiral.networked.PhysicsMoveGraphicalController;
import net.faintedge.spiral.networked.PhysicsSpatialGraphicalController;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsMoveObjectManager;
import net.faintedge.spiral.networked.sync.ss.SSPhysicsSpatialObjectManager;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import common.Mode;

public class TestSSPhysicsClient {
  
  private static final Logger logger = Logger.getLogger(TestSSPhysicsClient.class.getName());
  private static final float PTM = 10.0f;

  public static void main(String[] args) throws IOException, SlickException, InterruptedException {
//    CoolFormatter.init();
    Net.init();
    
    SSPhysicsGame game = new SSPhysicsGame(Mode.CLIENT);
    final AppGameContainer container = new AppGameContainer(game);
    container.setDisplayMode(800, 600, false);
    container.setAlwaysRender(true);
    container.setShowFPS(true);
    Runnable gameRunnable = new Runnable() {
      @Override
      public void run() {
        try {
          container.start();
        } catch (SlickException e) {
          e.printStackTrace();
        }
      }
    };
    new Thread(gameRunnable).start();
    
    JGNClient client = new JGNClient(new InetSocketAddress(InetAddress.getLocalHost(), 0), new InetSocketAddress(InetAddress.getLocalHost(), 0));
    
    PhysicsSpatialGraphicalController physicsSpatialController = new PhysicsSpatialGraphicalController(SSPhysicsGame.physics);
    SynchronizationManager physicsSpatialSyncManager = new SynchronizationManager(client, physicsSpatialController, (short) 2);
    
    PhysicsMoveGraphicalController physicsMoveController = new PhysicsMoveGraphicalController();
    SynchronizationManager physicsMoveManager = new SynchronizationManager(client, physicsMoveController, (short) 3);
    
    SSPhysicsSpatialObjectManager objectManager = new SSPhysicsSpatialObjectManager(Mode.CLIENT, game.getRootNode(), SSPhysicsGame.physics, PTM);
    objectManager.setSpatialSyncManager(physicsSpatialSyncManager);
    physicsSpatialSyncManager.addSyncObjectManager(objectManager);
    client.addMessageListener(objectManager);
    
    SSPhysicsMoveObjectManager moveManager = new SSPhysicsMoveObjectManager(Mode.CLIENT);
    moveManager.setMoveSyncManager(physicsMoveManager);
    physicsMoveManager.addSyncObjectManager(moveManager);
    client.addMessageListener(moveManager);

    game.setClient(client);
    game.setObjectManager(objectManager);
    JGN.createThread(client, physicsSpatialSyncManager, physicsMoveManager).start();
  }

}
