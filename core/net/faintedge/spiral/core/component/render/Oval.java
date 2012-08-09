package net.faintedge.spiral.core.component.render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Oval extends Geometry {
  
  private float width = 5;
  private float height = 5;
  
  public Oval() {
    super();
  }
  
  public Oval(Color color, float width, float height) {
    super(color);
    this.width = width;
    this.height = height;
  }
  
  @Override
  public void renderGeometry(Graphics g) {
    g.drawOval(-height/2, -width/2, height, width);
  }

  @Override
  public void renderGeometryFilled(Graphics g) {
    g.fillOval(-height/2, -width/2, height, width);
  }

  public float getWidth() {
    return width;
  }

  public void setWidth(float width) {
    this.width = width;
  }

  public float getHeight() {
    return height;
  }

  public void setHeight(float height) {
    this.height = height;
  }
  
}