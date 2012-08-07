package net.faintedge.spiral.fx;

import static org.junit.Assert.assertEquals;

import net.faintedge.spiral.fx.ColorBlender;

import org.junit.Test;
import org.newdawn.slick.Color;

public class TestColorBlender {
  
  @Test
  public void testWrap() {
    Color c1 = new Color(0.0f, 0.0f, 0.0f, 0.0f);
    Color c2 = new Color(1.0f, 1.0f, 1.0f, 1.0f);
    
    ColorBlender blender = new ColorBlender();
    blender.addMarker(0.25f, c1);
    blender.addMarker(0.45f, new Color(0, 25, 238, 0));
    blender.addMarker(0.75f, c2);

    assertEquals(new Color(0.75f, 0.75f, 0.75f, 0.75f), blender.getColor(0.875f));
    assertEquals(new Color(0.5f, 0.5f, 0.5f, 0.5f), blender.getColor(1.0f));
    assertEquals(new Color(0.5f, 0.5f, 0.5f, 0.5f), blender.getColor(0.0f));
    assertEquals(new Color(0.25f, 0.25f, 0.25f, 0.25f), blender.getColor(0.125f));
  }

}
