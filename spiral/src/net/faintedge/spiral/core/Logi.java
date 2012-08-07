package net.faintedge.spiral.core;


public class Logi {

  public static void info(String data) {
    System.out.println(data);
  }
  
  public static void info(String data, Object...args) {
    System.out.printf(data, args);
    System.out.println();
  }
  
}
