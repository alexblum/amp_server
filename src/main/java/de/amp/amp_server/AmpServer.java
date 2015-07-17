package de.amp.amp_server;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class AmpServer {

  private static final int PORT = 1337;

  public void start() {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    Server jettyServer = new Server(1337);
    jettyServer.setHandler(context);
    jettyServer.addBean(new AmpErrorHandler());

    ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
    jerseyServlet.setInitOrder(0);
    jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", RestInterface.class.getCanonicalName());

    try {
      jettyServer.start();
      jettyServer.join();
    } catch (Exception ex) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "fatal error: ", ex);
    } finally {
      jettyServer.destroy();
    }
  }
}
