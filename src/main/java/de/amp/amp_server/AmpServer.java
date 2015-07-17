package de.amp.amp_server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class AmpServer {

  public void start() {
    ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
    context.setContextPath("/");

    Server jettyServer = new Server(1337);
    jettyServer.setHandler(context);

    ServletHolder jerseyServlet = context.addServlet(ServletContainer.class, "/*");
    jerseyServlet.setInitOrder(0);

    // Tells the Jersey Servlet which REST service/class to load.
    jerseyServlet.setInitParameter(
            "jersey.config.server.provider.classnames",
            RestInterface.class.getCanonicalName());

    try {
      jettyServer.start();
      jettyServer.join();
    } catch (Exception ex) {
      ex.printStackTrace();
    } finally {
      jettyServer.destroy();
    }
  }
}
