package de.amp.amp_server.boundary.request;

import de.amp.amp_server.boundary.bean.Response;
import de.amp.amp_server.datasource.dao.UserDAO;
import de.amp.amp_server.datasource.entity.User;

public class GetDatabaseStats extends RequestMethod<String> {

  public GetDatabaseStats() {
    super(String.class);
  }

  @Override
  public Response process(final User user, final String payload) {

    switch (payload) {
      case "userCount":
        return new Response("count", Integer.toString(new UserDAO().countUsers()));
      default:
        return new Response("error", "unkown stat");
    }
  }
}
