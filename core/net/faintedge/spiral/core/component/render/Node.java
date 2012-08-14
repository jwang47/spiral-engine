package net.faintedge.spiral.core.component.render;

import java.util.ArrayList;
import java.util.List;

import net.faintedge.spiral.core.Renderable;

import org.newdawn.slick.Graphics;

public class Node implements Renderable {

  private List<Spatial> spatials = new ArrayList<Spatial>();

  public void add(Spatial spatial) {
    spatials.add(spatial);
  }

  public boolean remove(Spatial spatial) {
    return spatials.remove(spatial);
  }

  @Override
  public void render(Graphics g) {
    for (Spatial spatial : spatials) {
      spatial.render(g);
    }
  }

}
