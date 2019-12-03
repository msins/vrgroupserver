package edu.vrgroup.rest;

import com.google.gson.annotations.Expose;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.util.JsonUtils;
import java.util.List;
import java.util.Map;
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
    GameResponse response = new GameResponse(new Game(game));

    if (!response.gameExists()) {
      return Response.status(404).entity("There is no game with that name.").build();
    }

    response.get();

    String output = JsonUtils.toJson(response);
    return Response.status(200).entity(output).build();
  }

  private static class GameResponse {

    @Expose
    private List<Scenario> scenarios;
    @Expose
    private List<Question> questions;
    private Game game;

    public GameResponse(Game game) {
      this.game = game;
    }

    public boolean gameExists() {
      return DaoProvider.getDao().containsGame(game);
    }

    public void get() {
      questions = DaoProvider.getDao().getQuestions(game);
      scenarios = List.of(Scenario.DEFAULT);
    }
  }

}
