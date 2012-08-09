package util;

public class Log {

  public static void warn(String text) {
    System.out.println("WARNING: " + text);
  }

  public static final void info(String text) {
    System.out.println(text);
  }

  public static final void trace(String text) {
    System.out.println(text);
  }
  
}
