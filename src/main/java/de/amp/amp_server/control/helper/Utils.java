package de.amp.amp_server.datasource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {

  static public Properties readProperties(String filename) throws IOException {
    Properties props = new Properties();
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream stream = loader.getResourceAsStream(filename);
    props.load(stream);
    return props;
  }
}
