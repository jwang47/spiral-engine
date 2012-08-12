package net.faintedge.spiral.networked.sync;

import net.faintedge.spiral.core.Renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

import com.artemis.Component;

public class SyncObjectDebugRender<T> implements Renderable {

  private SyncObject<T> syncObject;

  public SyncObjectDebugRender(SyncObject<T> object) {
    this.syncObject = object;
  }
  
  @Override
  public void render(Graphics g) {
    g.setColor(Color.orange);
    g.drawString("owner=" + syncObject.getOwnerId() + " objId=" + syncObject.getSyncObjectId(), 0, 0);
  }

}
