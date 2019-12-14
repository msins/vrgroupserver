package edu.vrgroup.rest;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.model.User;
import edu.vrgroup.util.JsonUtils;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import javax.persistence.criteria.CriteriaBuilder.In;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
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
  public Response getGameInformation(@PathParam("game") String name) {

    logger.log(Level.INFO, "GET [" + request.getRemoteAddr() + "] " + name);

    Game game = DaoHelper.getGame(name);
    if (game == null) {
      return Response.status(404).entity("There is no game with that name.").build();
    }

    GameResponse response = new GameResponse(game);
    response.get();

    String output = JsonUtils.toJson(response);
    return Response.status(200).entity(output).build();
  }

  @POST
  @Path("{game}")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  public Response submitUserAnswerToDb(@PathParam("game") String name, String body) {

    logger.log(Level.INFO, "POST [" + request.getRemoteAddr() + "] " + name);

    Game game = DaoHelper.getGame(name);
    if (game == null) {
      return Response.status(404).entity("There is no game with that name.").build();
    }

    AnswerResponse r = JsonUtils.fromJson(body, AnswerResponse.class);

    if (!DaoHelper.addAnswer(game, r.scenario, r.question, r.choice, r.user, request.getRemoteAddr())) {
      return Response.status(503).entity("Problem with server.").build();
    }

    return Response.status(201).entity("Successfully added to db.").build();
  }

  /**
   * Answer received.
   */
  public static class AnswerResponse {

    public User user;
    public Scenario scenario;
    public Question question;
    public Choice choice;

    @Override
    public String toString() {
      return MoreObjects.toStringHelper(this)
          .add("user", user)
          .add("scenario", scenario)
          .add("question", question)
          .add("choice", choice)
          .toString();
    }
  }

  /**
   * Response given.
   */
  private static class GameResponse {

    @Expose
    private List<Scenario> scenarios;

    @Expose
    private List<Question> questions;

    private Game game;

    public GameResponse(Game game) {
      this.game = game;
    }

    public void get() {
      questions = DaoProvider.getDao().getQuestions(game);
      for (Question question : questions) {
        Collections.sort(question.getChoices());
      }
      scenarios = List.of(Scenario.DEFAULT);
    }
  }

  private static final class DaoHelper {

    static Game getGame(String name) {
      return DaoProvider.getDao().getGame(name);
    }

    static boolean addAnswer(Game game, Scenario scenario, Question question, Choice choice, User user, String IPv4) {
      try {
        DaoProvider.getDao().addUser(user);
        DaoProvider.getDao().addAnswer(game, scenario, question, choice, user, Timestamp.from(Instant.now()), IPv4);
      } catch (Exception e) {
        return false;
      }

      return true;
    }
  }
}
