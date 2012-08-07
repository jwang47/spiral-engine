package net.faintedge.spiral.fx;

import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public class LightScreen {
  
  public static final Color DAWN = new Color(156, 151, 171, 64);
  public static final Color SUNRISE = new Color(254, 137, 68, 64);
  public static final Color MORNING = new Color(205, 229, 239, 32);
  public static final Color AFTERNOON = new Color(241, 229, 207, 16);
  public static final Color EVENING = new Color(68, 30, 43, 128);
  public static final Color SUNSET = new Color(254, 137, 68, 32);
  public static final Color DUSK = new Color(79, 107, 147, 128);
  
  private GameTimer timer;
  private GameContainer container;
  private ColorBlender blender;

  public LightScreen(GameContainer container, GameTimer timer) {
    this.container = container;
    this.timer = timer;
    blender = new ColorBlender();
    blender.addMarker(0.00f, DAWN);
    //blender.addMarker(0.15f, SUNRISE);
    blender.addMarker(0.30f, MORNING);
    blender.addMarker(0.45f, AFTERNOON);
    blender.addMarker(0.60f, EVENING);
    //blender.addMarker(0.75f, SUNSET);
    blender.addMarker(0.90f, DUSK);
  }
  
  private Color getColorNow() {
    return blender.getColor(timer.getHoursFloat()/24.0f);
  }
  
  public void render(Graphics g) {
    g.setColor(getColorNow());
    g.fillRect(0, 0, container.getWidth(), container.getHeight());
    
    float derp = timer.getHoursFloat()/24.0f;
    g.setColor(Color.orange);
    g.drawString("value " + derp, 10, 70);
    g.drawString("value " + getColorNow(), 10, 80);
  }

}
