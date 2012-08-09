package net.faintedge.spiral.core.component.render.ext;

import java.util.Iterator;
import java.util.LinkedList;

import net.faintedge.spiral.core.Renderable;
import net.faintedge.spiral.core.Vector2;

import org.newdawn.slick.Graphics;

public class Graph implements Renderable {
  
  private long minX = Long.MAX_VALUE;
  private long maxX = Long.MIN_VALUE;
  private long minY = Long.MAX_VALUE;
  private long maxY = Long.MIN_VALUE;
  
  private float width;
  private float height;
  
  private LinkedList<Vector2> points = new LinkedList<Vector2>();
  
  public Graph(float width, float height) {
    this.width = width;
    this.height = height;
  }

  @Override
  public void render(Graphics g) {
    g.translate(-minX, 0);
    
    Iterator<Vector2> it = points.iterator();
    while (it.hasNext()) {
      Vector2 from = it.next();
      if (!it.hasNext()) break;
      Vector2 to = it.next();
      
      float scaleX = (maxX - minX) / (width);
      g.scale(1/scaleX, 1);
      g.drawLine(from.getX(), from.getY(), to.getX(), to.getY());
      g.scale(scaleX, 1);
      System.out.println("from " + from + " to " + to);
      System.out.println("1/scale= " + 1/scaleX);
    }
    
    
    g.translate(minX, 0);
  }

  public void plot(long x, long y) {
    if (x < minX) { minX = x; }
    if (x > maxX) { maxX = x; }
    if (y < minY) { minY = y; }
    if (y > maxY) { maxY = y; }
    points.addFirst(new Vector2(x, y));
  }

}
