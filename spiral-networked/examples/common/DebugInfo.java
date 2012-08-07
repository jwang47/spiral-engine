package common;

import java.util.HashMap;
import java.util.Map;

public class DebugInfo {

  private static DebugInfo instance = null;
  private Map<String, Object> info = new HashMap<String, Object>();
  
  private DebugInfo() {
    
  }
  
  public static DebugInfo getInstance() {
    if (instance == null) {
      instance = new DebugInfo();
    }
    return instance;
  }
  
  public Object getInfo(String key) {
    return info.get(key);
  }
  
  public void setInfo(String key, Object object) {
    info.put(key, object);
  }
  
}
