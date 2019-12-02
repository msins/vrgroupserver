package edu.vrgroup.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1/games")
public class GamesService {

  @GET
  @Path("{game}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGameInformation(@PathParam("game") String game) {
    String output = "{\n  \"game\": " + game + "\n}";
    return Response.status(200).entity(output).build();
  }
}
