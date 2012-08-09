package net.faintedge.spiral.core.component.render;

import net.faintedge.spiral.core.Renderable;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public abstract class Geometry implements Renderable {

  private ColorWrapper color;
  private boolean filled;

  public Geometry() {
    super();
    this.color = new ColorWrapper(Color.gray);
    this.filled = false;
  }

  public Geometry(Color color) {
    super();
    this.color = new ColorWrapper(color);
    this.filled = false;
  }

  public abstract void renderGeometry(Graphics g);

  public abstract void renderGeometryFilled(Graphics g);

  public void render(Graphics g) {
    g.setColor(color);
    renderGeometry(g);
  }

  public boolean isFilled() {
    return filled;
  }

  public void setFilled(boolean filled) {
    this.filled = filled;
  }

  public ColorWrapper getColor() {
    return color;
  }

  public void setColor(ColorWrapper color) {
    this.color = color;
  }

}
