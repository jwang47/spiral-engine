package net.faintedge.spiral.fx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import net.faintedge.spiral.core.Logi;

import org.newdawn.slick.Color;

public class ColorBlender {
  
  private static final Color BLANK = new Color(0, 0, 0, 0);
  private Color interp;
  private List<Marker> markers;
  
  public ColorBlender() {
    interp = new Color(0, 0, 0, 0);
    markers = new ArrayList<Marker>();
  }
  
  public void addMarker(float position, Color color) {
    Marker m = new Marker(position, color);
    
    if (markers.size() == 0) {
      markers.add(m);
    } else {
      markers.add(m);
      Collections.sort(markers, new Comparator<Marker>() {
        @Override
        public int compare(Marker m1, Marker m2) {
          if (m2.position < m1.position) {
            return 1;
          } else if (m1.position < m2.position) {
            return -1;
          } else {
            return 0;
          }
        }
        
      });
    }
  }
  
  private Color interp(Marker curr, Marker next, float position) {
    float positionInterp = (position - curr.position)/(next.position - curr.position);
    interp.r = curr.color.r + ((next.color.r - curr.color.r) * positionInterp);
    interp.g = curr.color.g + ((next.color.g - curr.color.g) * positionInterp);
    interp.b = curr.color.b + ((next.color.b - curr.color.b) * positionInterp);
    interp.a = curr.color.a + ((next.color.a - curr.color.a) * positionInterp);
    return interp;
  }

  private Color interpWrap(Marker curr, Marker next, float position) {
    float positionInterp = (position - curr.position)/(next.position + 1.0f - curr.position);
    interp.r = curr.color.r + ((next.color.r - curr.color.r) * positionInterp);
    interp.g = curr.color.g + ((next.color.g - curr.color.g) * positionInterp);
    interp.b = curr.color.b + ((next.color.b - curr.color.b) * positionInterp);
    interp.a = curr.color.a + ((next.color.a - curr.color.a) * positionInterp);
    return interp;
  }
  
  /**
   * @param value between 0.0f and 1.0f
   * @return the color at this point
   */
  public Color getColor(float value) {
    if (markers.size() == 0) {
      return BLANK;
    }
    
    if (markers.size() == 1) {
      return markers.get(0).color;
    }
    
    if (value <= markers.get(0).position) {
      // interp from last marker to first marker
      return interpWrap(markers.get(markers.size() - 1), markers.get(0), value + 1.0f);
    }
    
    for (int i = 0; i < markers.size() - 1; i++) {
      Marker curr = markers.get(i);
      Marker next = markers.get(i+1);
      if (value >= curr.position && value <= next.position) {
        // interpolate between curr and next colors
        return interp(curr, next, value);
      }
    }
    
    return interpWrap(markers.get(markers.size() - 1), markers.get(0), value);
  }
  
  private class Marker {
    protected float position;
    protected Color color;
    
    public Marker(float position, Color color) {
      this.position = position;
      this.color = color;
    }
    
    public String toString() {
      return String.format("{%f, color(%f, %f, %f, %f)", position, color.r, color.g, color.b, color.a);
    }
  }

}
