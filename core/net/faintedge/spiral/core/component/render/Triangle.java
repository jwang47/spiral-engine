package net.faintedge.spiral.core.component.render;

import org.newdawn.slick.Color;
import org.newdawn.slick.Graphics;

public class Triangle extends Geometry {

	private float width = 5;
	private float height = 5;
	
	public Triangle() {
    super();
	}
	
	public Triangle(Color color, float width, float height) {
		super(color);
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void renderGeometry(Graphics g) {
		g.drawLine(-height/2, width/2, -height/2, -width/2);
		g.drawLine(-height/2, width/2, height/2, 0);
		g.drawLine(-height/2, -width/2, height/2, 0);
	}

  @Override
  public void renderGeometryFilled(Graphics g) {
    throw new UnsupportedOperationException();
  }
}
