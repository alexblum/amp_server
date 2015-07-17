package de.amp.amp_server.boundary;

import de.amp.amp_server.boundary.bean.Request;
import de.amp.amp_server.boundary.bean.Response;
import de.amp.amp_server.datasource.dao.UserDAO;

public class RequestHandler {

  Response handle(Request request) {
    switch (request.getMethod()) {
      case "getDatabaseStats":
        return getDatabaseStats(request.getPayload());
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

}
