package net.faintedge.spiral.networked.event;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

public class NetEventSystem extends Listener {
  
  private Server server;
  private Client client;
  
  public NetEventSystem() {
    
  }

  public void setServer(Server server) {
    this.server = server;
    server.addListener(this);
  }

  public void setClient(Client client) {
    this.client = client;
    client.addListener(this);
  }

}
