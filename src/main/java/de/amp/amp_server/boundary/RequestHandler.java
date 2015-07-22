package de.amp.amp_server.boundary;

import de.amp.amp_server.boundary.bean.Request;
import de.amp.amp_server.boundary.bean.Response;
import de.amp.amp_server.control.App;
import de.amp.amp_server.datasource.dao.UserDAO;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestHandler {

  Response handle(Request request) {
    switch (request.getMethod()) {
      case "getDatabaseStats":
        return getDatabaseStats(request.getPayload());
      case "changeState":
        return changeState(request);
      default:
        return new Response("error", "unknown method");
    }
  }

  private Response getDatabaseStats(String payload) {
    switch (payload) {
      case "userCount":
        return new Response("count", Integer.toString(new UserDAO().countUsers()));
      default:
        return new Response("error", "unkown stat");
    }
  }

  private Response changeState(Request request) {
    if (!request.getUser().equals("admin")) {
      Logger.getLogger(this.getClass().getName()).log(Level.INFO, "user ''{0}'' not authorized to change state ''{1}''", new Object[]{request.getUser(), request.getPayload()});
      return new Response("error", "not authorized");
    }
    Logger.getLogger(this.getClass().getName()).log(Level.INFO, "user ''{0}'' trying to change state ''{1}''", new Object[]{request.getUser(), request.getPayload()});
    switch (request.getPayload()) {
      case "shutdown":
        App.getSingleton().interruptMainLoop();
        return new Response("ok", "shutting down");
      default:
        return new Response("error", "unkown state");
    }
  }

}
