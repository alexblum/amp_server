package de.amp.amp_server.boundary;

import de.amp.amp_server.boundary.bean.Request;
import de.amp.amp_server.boundary.bean.Response;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/rest")
public class RestInterface {

  @GET
  @Path("hello")
  @Produces(MediaType.APPLICATION_JSON)
  public String hello() {
    return "hi there :)";
  }

  @POST
  @Path("interface")
  @Produces(MediaType.APPLICATION_JSON)
  @Consumes(MediaType.APPLICATION_JSON)
  public Response entry(Request request) {
    System.out.println("received post!");

    if (Authenticator.validRequest(request)) {
      Response response = new Response();
      response.setResult("authenticated");
      return response;
    }

    Response response = new Response();
    response.setResult("auth doof");
    return response;
  }
}
