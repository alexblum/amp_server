package de.amp.amp_server;

import de.amp.amp_server.boundary.AmpServer;

public class Main {

  public static void main(String[] args) {
    try {
      AmpServer server = new AmpServer();
      server.start();
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
