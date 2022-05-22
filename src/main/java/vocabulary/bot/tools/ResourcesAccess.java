package vocabulary.bot.tools;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ResourcesAccess {

  private static final String path = "src/main/resources/config.properties";

  public static String getFromResources(String key) {
    try {
      FileReader reader = new FileReader(path);
      Properties prop = new Properties();
      prop.load(reader);
      return prop.getProperty(key);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return "";
  }

}
