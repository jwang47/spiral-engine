package net.faintedge.spiral.networked.sync.ss;

import java.util.HashMap;
import java.util.Map;

import net.faintedge.spiral.core.Spatial;

public class SSCache {

  public static final Map<Short, Map<Short, Spatial>> spatialsByPlayerId = new HashMap<Short, Map<Short, Spatial>>();
  public static final Map<Short, Spatial> spatialsBySyncId = new HashMap<Short, Spatial>();
  
}
