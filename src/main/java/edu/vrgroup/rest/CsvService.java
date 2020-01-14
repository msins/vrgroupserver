package edu.vrgroup.rest;

import com.opencsv.CSVWriter;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Game;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/csv")
public class CsvService extends Application {

  private static final Logger logger = LogManager.getLogger(CsvService.class);

  @Context
  HttpServletRequest request;

  @GET
  @Path("{game}")
  @Produces(MediaType.TEXT_PLAIN)
  public Response getGameInformation(@PathParam("game") String name) {

    logger.log(Level.INFO, "GET [" + request.getRemoteAddr() + "] " + name);

    Game game = DaoHelper.getGame(name);

    if (game == null) {
      return Response.status(404).entity("There is no game with that name.").build();
    }

    /*
    This part is badly optimized, it creates a file and then reads it(every time). You can easily optimize it if you
    just load it to ram.
     */
    CsvHelper csv = new CsvHelper(game);
    java.nio.file.Path csvPath = Paths.get("csv", game.getName() + "_answers.csv");
    csv.writeToFile(csvPath.toFile());
    String csvStr;
    try {
      csvStr = Files.readString(csvPath);
    } catch (IOException e) {
      return Response.status(404).entity("Failed to generate csv.").build();
    }

    return Response.status(200).entity(csvStr).build();
  }

  private static final class CsvHelper {

    private Game game;

    public CsvHelper(Game game) {
      this.game = game;
    }

    public void writeToFile(File file) {
      int offset = 0;
      int limit = 5000;

      CSVWriter writer = null;
      try {
        writer = new CSVWriter(new BufferedWriter(new FileWriter(file), '\t'));
        writer.writeNext(header());

        AtomicInteger count = new AtomicInteger();
        while (true) {
          Stream<Answer> answers = DaoHelper.getAnswers(game, offset, limit);
          answers.map(this::formatAnswer)
              .peek(e -> count.incrementAndGet())
              .forEach(writer::writeNext);

          if (count.get() < limit) {
            break;
          }

          offset += limit;
          writer.flush();
          writer.close();
        }
      } catch (IOException e) {
        logger.log(Level.ERROR, e.getMessage());
      } finally {
        IOUtils.closeQuietly(writer);
      }
    }

    private String[] formatAnswer(Answer a) {
      return new String[]{
          a.getTimestamp().toString(),
          a.getQuestion().getText(),
          a.getScenario().getName(),
          a.getGame().getName(),
          a.getUser().toString(),
          a.getChoice().getValue(),
          a.getIP()
      };
    }

    private static final String[] header = new String[]{
        "timestamp", "question", "scenario", "game", "user", "choice", "Ip"
    };

    private String[] header() {
      return header;
    }
  }

  private static final class DaoHelper {

    static Game getGame(String name) {
      return DaoProvider.getDao().getGame(name);
    }

    static Stream<Answer> getAnswers(Game game, int offset, int limit) {
      return DaoProvider.getDao().getAnswers(game, offset, limit);
    }
  }
}
