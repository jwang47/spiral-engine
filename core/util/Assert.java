package util;

public class Assert {

  public static final void isTrue(boolean something) {
    if (!something) {
      throw new RuntimeException();
    }
  }
  
  public static final void isTrue(boolean something, String message) {
    if (!something) {
      throw new RuntimeException(message);
    }
  }
  
}
