package net.faintedge.spiral.networked;

import java.nio.channels.UnsupportedAddressTypeException;
import java.util.HashMap;
import java.util.Properties;

import net.faintedge.spiral.core.Spatial;
import net.faintedge.spiral.spatial.Geometry;
import net.faintedge.spiral.spatial.Oval;
import net.faintedge.spiral.spatial.Rectangle;

import org.newdawn.slick.Color;

import com.captiveimagination.jgn.synchronization.message.SynchronizeCreateMessage;

public class SpatialSyncCreate extends SynchronizeCreateMessage {

  private String name;
  private Class<? extends Spatial> type;
  private Properties props;
  private HashMap<String, Object> params;
  
  public SpatialSyncCreate() {
    params = new HashMap<String, Object>();
  }
  
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Class<? extends Spatial> getType() {
    return type;
  }

  public void setType(Class<? extends Spatial> type) {
    this.type = type;
  }

  public void setParam(String key, Object value) {
    params.put(key, value);
  }
  
  public Object getParam(String key, Object def) {
    return params.get(key) != null ? params.get(key) : def;
  }

  public static Spatial createSpatial(SpatialSyncCreate msg) {
    assert msg != null : "msg was null";
    Spatial spatial = null;
    if (msg.getType() == Oval.class) {
      spatial = new Oval(
        (Color) msg.getParam("color", Color.white),
        (float) msg.getParam("width", 50),
        (float) msg.getParam("height", 50));
    } else if (msg.getType() == Rectangle.class) {
      spatial = new Rectangle(
        (Color) msg.getParam("color", Color.white),
        (float) msg.getParam("width", 50),
        (float) msg.getParam("height", 50));
    }
    
    if (spatial == null) {
      throw new UnsupportedOperationException("unsupported type of spatial: " + msg.getType());
    }
    spatial.setName(msg.name);
    return spatial;
  }

  public static SpatialSyncCreate fromSpatial(Spatial spatial) {
    SpatialSyncCreate msg = new SpatialSyncCreate();
    msg.name = spatial.getName();
    msg.type = spatial.getClass();
    if (spatial instanceof Geometry) {
      Geometry geometry = (Geometry) spatial;
      msg.setParam("color", geometry.getColor());
      if (geometry instanceof Oval) {
        Oval oval = (Oval) geometry;
        msg.setParam("width", oval.getWidth());
        msg.setParam("height", oval.getHeight());
      } else if (geometry instanceof Rectangle) {
        Rectangle rect = (Rectangle) geometry;
        msg.setParam("width", rect.getWidth());
        msg.setParam("height", rect.getHeight());
      } else {
        throw new UnsupportedOperationException();
      }
    } else {
      throw new UnsupportedOperationException();
    }
    return msg;
  }
  
}
