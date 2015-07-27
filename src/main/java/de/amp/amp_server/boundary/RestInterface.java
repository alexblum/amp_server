package de.amp.amp_server.boundary;

import de.amp.amp_server.boundary.bean.AuthenticationResult;
import de.amp.amp_server.boundary.bean.Request;
import de.amp.amp_server.boundary.bean.Response;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
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
  public Response entry(final Request request, @Context final HttpServletRequest httpServletRequest) {
    System.out.println("received post!");
    Response tarpitResponse = Tarpit.validateRequest(request, httpServletRequest);
    if (tarpitResponse != null) {
      return tarpitResponse;
    }

    AuthenticationResult authenticationResult = Authenticator.validRequest(request);
    if (authenticationResult.isAuthenticated()) {
      Tarpit.registerSuccessfulRequest(request, httpServletRequest);
      return new RequestHandler().handle(request, authenticationResult.getUser());
    } else {
      Tarpit.registerDeniedRequest(request, httpServletRequest);
    }

    Response response = new Response();
    response.setResult("auth failed");
    return response;
  }
}
