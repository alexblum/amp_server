package de.amp.amp_server.boundary.request;

import de.amp.amp_server.boundary.bean.Response;
import de.amp.amp_server.control.App;
import de.amp.amp_server.datasource.entity.User;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChangeState extends RequestMethod<ChangeStateEntity> {

  public ChangeState() {
    super(ChangeStateEntity.class);
  }

  @Override
  public Response process(final User user, final ChangeStateEntity changeStateEntity) {
    if (user.getUserGroup() != 1) {
      Logger.getLogger(this.getClass().getName()).log(Level.INFO, "user ''{0}'' not authorized to change state", new Object[]{user.getEmail()});
      return new Response("error", "not authorized");
    }

    if (changeStateEntity.isShutdown()) {
      Logger.getLogger(this.getClass().getName()).warning("shutting down !!");
      App.getSingleton().interruptMainLoop();
      return new Response("ok", "shutting down");
    }

    return new Response("error", "unkown state");
  }
}
