package net.faintedge.spiral.spatial;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Line extends Geometry {

  private float fromX;
  private float fromY;
  private float toX;
  private float toY;

  public Line() {
    super();
  }

  public Line(Color color, float fromX, float fromY, float toX, float toY) {
    super(color);
    this.fromX = fromX;
    this.fromY = fromY;
    this.toX = toX;
    this.toY = toY;
  }

  @Override
  public void renderGeometry(Graphics g) {
    g.setLineWidth(5f);
    g.drawLine(fromX, fromY, toX, toY);
    g.setLineWidth(1);
  }

  @Override
  public void renderGeometryFilled(Graphics g) {
    g.drawLine(fromX, fromY, toX, toY);
  }

  public void setFrom(float x, float y) {
    this.fromX = x;
    this.fromY = y;
  }

  public void setTo(float x, float y) {
    this.toX = x;
    this.toY = y;
  }

}
