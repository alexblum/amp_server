package de.amp.amp_server;

import de.amp.amp_server.boundary.AmpServer;
import de.amp.amp_server.control.App;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

  private static final Logger LOGGER = Logger.getLogger(Main.class.getName());

  public static void main(String[] args) {
    try {
      LOGGER.info("preparing server");
      AmpServer server = new AmpServer();

      LOGGER.info("preparing app");
      App.init();

      LOGGER.info("starting server");
      server.start();

      LOGGER.info("starting app");
      App.getSingleton().enterMainLoop();

      LOGGER.info("stopping server");
      server.stop();

      LOGGER.info("stopping app");
      App.getSingleton().stop();

      LOGGER.info("goodbye :)");
    } catch (Exception ex) {
      LOGGER.log(Level.SEVERE, null, ex);
    }
  }
}
