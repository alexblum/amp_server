package de.amp.amp_server;

import de.amp.amp_server.boundary.AmpServer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

  public static void main(String[] args) {
    try {
      AmpServer server = new AmpServer();
      server.start();
    } catch (Exception ex) {
      Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
