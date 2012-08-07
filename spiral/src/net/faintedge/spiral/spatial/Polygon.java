package net.faintedge.spiral.spatial;

import net.faintedge.spiral.core.Vector2;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Polygon extends Geometry {
  
  private Vector2[] vertices;
  
  public Polygon() {
    super();
  }
  
  public Polygon(Color color, Vector2[] vertices) {
    super(color);
    this.vertices = vertices;
  }

  @Override
  public void renderGeometry(Graphics g) {
    for (int i = 0; i < vertices.length - 1; i++) {
      Vector2 from = vertices[i];
      Vector2 to = vertices[i+1];
      g.drawLine(from.getX(), from.getY(), to.getX(), to.getY());
    }
    
    Vector2 from = vertices[vertices.length - 1];
    Vector2 to = vertices[0];
    g.drawLine(from.getX(), from.getY(), to.getX(), to.getY());
  }

  @Override
  public void renderGeometryFilled(Graphics g) {
    throw new UnsupportedOperationException();
  }
}
