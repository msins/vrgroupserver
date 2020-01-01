package edu.vrgroup.ui.providers;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import java.util.stream.Stream;

public final class AnswersGridDataProvider extends AbstractBackEndDataProvider<Answer, Object> {

  private Game game;
  private Scenario scenario;
  private Question question;

  public AnswersGridDataProvider(Game game, Scenario scenario, Question question) {
    this.game = game;
    this.scenario = scenario;
    this.question = question;
  }

  @Override
  protected Stream<Answer> fetchFromBackEnd(Query<Answer, Object> query) {
    return DaoProvider.getDao().getAnswers(game, scenario, question, query.getOffset(), query.getLimit());
  }

  @Override
  protected int sizeInBackEnd(Query<Answer, Object> query) {
    return DaoProvider.getDao().getAnswersCount(game, scenario, question);
  }
}
