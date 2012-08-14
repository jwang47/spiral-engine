package net.faintedge.spiral.core.component.render;

import net.faintedge.spiral.core.Renderable;
import net.faintedge.spiral.core.Vector2;

import org.newdawn.slick.Graphics;

public class Spatial implements Renderable {

  private Renderable render;
  
  private Vector2 translation;
  private float rotation;
  private float scale;
  
  public Spatial(Renderable render) {
    this.render = render;
    translation = new Vector2();
    rotation = 0;
    scale = 1;
  }

  @Override
  public void render(Graphics g) {
    g.rotate(0, 0, rotation);
    g.translate(translation.getX(), translation.getY());
    render.render(g);
    g.translate(-translation.getX(), -translation.getY());
    g.rotate(0, 0, -rotation);
  }

}