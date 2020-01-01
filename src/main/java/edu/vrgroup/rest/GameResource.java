package edu.vrgroup.rest;

import com.google.gson.annotations.Expose;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Response given.
 */
class GameResource {

  @Expose
  private List<ScenarioDto> scenarios;

  private Game game;

  public GameResource(Game game) {
    this.game = game;
  }

  public void get() {
    scenarios = new ArrayList<>();
    Stream<Scenario> scenarios = DaoProvider.getDao().getScenarios(game);
    for (Scenario scenario : (Iterable<Scenario>) scenarios::iterator) {
      List<Question> questions = DaoProvider.getDao().getQuestions(scenario).collect(Collectors.toList());
      for (Question question : questions) {
        Collections.sort(question.getChoices());
      }

      this.scenarios.add(new ScenarioDto(scenario, questions));
    }
  }

  /**
   * Helper class to simulate Scenario which has List of questions.
   */
  private static class ScenarioDto {

    @Expose
    Scenario scenario;

    @Expose
    List<Question> questions;

    public ScenarioDto(Scenario scenario, List<Question> questions) {
      this.scenario = scenario;
      this.questions = questions;
    }
  }

}
