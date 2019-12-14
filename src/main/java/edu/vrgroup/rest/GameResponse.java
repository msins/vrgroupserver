package edu.vrgroup.rest;

import com.google.gson.annotations.Expose;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import java.util.Collections;
import java.util.List;

/**
 * Response given.
 */
class GameResponse {

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
