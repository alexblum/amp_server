package de.amp.amp_server.boundary;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.servlet.ServletContainer;

public class AmpServer {

  private static final int PORT = 1337;

  private final Server jettyServer;

  public AmpServer() {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    jettyServer = new Server(PORT);
    jettyServer.setHandler(context);
    jettyServer.addBean(new AmpErrorHandler());

    ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
    jerseyServlet.setInitOrder(0);
    jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", RestInterface.class.getCanonicalName());

    FilterHolder filterHolder = new FilterHolder(CrossOriginFilter.class);
    filterHolder.setInitParameter("allowedOrigins", "*");
    filterHolder.setInitParameter("allowedMethods", "GET,POST");
    context.addFilter(filterHolder, "/*", null);
  }

  public void start() {
    try {
      jettyServer.start();
//      jettyServer.join();
    } catch (Exception ex) {
      Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, "fatal error: ", ex);
      jettyServer.destroy();
    }
  }

  public void stop() {
    try {
      jettyServer.stop();
    } catch (Exception ex) {
      Logger.getLogger(AmpServer.class.getName()).log(Level.SEVERE, null, ex);
    } finally {
      jettyServer.destroy();
    }
  }
}
