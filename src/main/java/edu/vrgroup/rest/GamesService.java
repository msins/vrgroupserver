package edu.vrgroup.rest;

import com.google.gson.annotations.Expose;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.util.JsonUtils;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/v1/games")
public class GamesService {

  private static final Logger logger = LogManager.getLogger(GamesService.class);

  @Context
  HttpServletRequest request;

  @GET
  @Path("{game}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getGameInformation(@PathParam("game") String game) {
    GameResponse response = new GameResponse(game);

    logger.log(Level.INFO, "[" + request.getRemoteAddr() + "] " + game);
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
    private TreeSet<Question> questions;

    private Game game;
    private String gameName;

    public GameResponse(String gameName) {
      this.gameName = gameName;
    }

    public boolean gameExists() {
      this.game = DaoProvider.getDao().getGame(gameName);
      return this.game != null;
    }

    public void get() {
      questions = new TreeSet<>(DaoProvider.getDao().getQuestions(game));
      scenarios = List.of(Scenario.DEFAULT);
    }
  }

}
