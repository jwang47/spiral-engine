package net.faintedge.spiral.core;

import net.faintedge.spiral.spatial.Node;

public class Dumper {
  
  public static void dump(Spatial spatial) {
    dump(spatial, "");
  }
  
  private static void dump(Spatial spatial, String prefix) {
    if (spatial == null) {
      System.out.println("null");
      return;
    }
    System.out.println(prefix + spatial.getName() + ":" + spatial.getClass());
    if (spatial.getControls().size() > 0) {
      System.out.println(prefix + "  [controls]");
      for (Control control : spatial.getControls()) {
        System.out.println(prefix + "  " + control.getClass());
      }
    }
    if (spatial instanceof Node && ((Node) spatial).getChildren().size() > 0) {
      System.out.println(prefix + "  [children]");
      Node node = (Node) spatial;
      for (Spatial child : node.getChildren()) {
        dump(child, prefix + "  ");
      }
    }
  }

}
