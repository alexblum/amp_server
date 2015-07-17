package de.amp.amp_server;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rest")
public class RestInterface {

  @GET
  @Path("interface")
  @Produces(MediaType.TEXT_PLAIN)
  public String test() {
    return "hello rest of world / world of rest";
  }
}
