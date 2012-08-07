package physics;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import net.faintedge.spiral.networked.Net;
import net.faintedge.spiral.networked.PhysicsSpatialGraphicalController;
import net.faintedge.spiral.networked.PhysicsSpatialSyncCreate;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;

import com.captiveimagination.jgn.JGN;
import com.captiveimagination.jgn.clientserver.JGNClient;
import com.captiveimagination.jgn.synchronization.SynchronizationManager;
import common.CoolFormatter;
import common.Mode;

public class TestPhysicsClient extends PhysicsGame {

  private static final Logger logger = Logger.getLogger(TestPhysicsClient.class.getName());
  
  public TestPhysicsClient(Mode mode) {
    super(mode);
  }

  @Override
  public void init(GameContainer container) throws SlickException {
    super.init(container);
    
    try {
//    CoolFormatter.init();
      Net.init();
      
      PhysicsSpatialGraphicalController controller = new PhysicsSpatialGraphicalController(PhysicsGame.physics);
      
      InetAddress address = InetAddress.getLocalHost();
      InetSocketAddress serverReliable = new InetSocketAddress(address, Net.TCP_PORT);
      InetSocketAddress serverFast = new InetSocketAddress(address, Net.UDP_PORT);
      JGNClient client = new JGNClient(new InetSocketAddress(InetAddress.getLocalHost(), 0), new InetSocketAddress(InetAddress.getLocalHost(), 0));
      
      // sync manager
      SynchronizationManager clientSyncManager = new SynchronizationManager(client, controller);
      clientSyncManager.addSyncObjectManager(new PhysicsManager(this.getRootNode(), PhysicsGame.physics));
      this.setSyncManager(clientSyncManager);
      JGN.createThread(client, clientSyncManager).start();
      client.connectAndWait(serverReliable, serverFast, 1000);
      System.out.println("CONNECTED!");
      
      logger.info("registering player");
      clientSyncManager.register(this.getPlayer(), PhysicsSpatialSyncCreate.fromSpatial(this.getPlayer()), 10);
    } catch(IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws IOException, SlickException, InterruptedException {
    PhysicsGame game = new TestPhysicsClient(Mode.CLIENT);
    final AppGameContainer container = new AppGameContainer(game);
    container.setDisplayMode(800, 600, false);
    container.setAlwaysRender(true);
    container.setShowFPS(true);
    container.start();
  }

}
