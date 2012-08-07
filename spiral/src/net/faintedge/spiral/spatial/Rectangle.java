package net.faintedge.spiral.spatial;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;


public class Rectangle extends Geometry {
	
	private float width = 5;
	private float height = 5;
	
	public Rectangle() {
    super();
	}
	
	public Rectangle(Color color, float width, float height) {
		super(color);
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void renderGeometry(Graphics g) {
		g.drawRect(-width/2, -height/2, width, height);
	}

  @Override
  public void renderGeometryFilled(Graphics g) {
    g.fillRect(-width/2, -height/2, width, height);
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
