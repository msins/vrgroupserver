package edu.vrgroup.ui.providers;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import java.util.stream.Stream;

public class QuestionsProvider extends AbstractBackEndDataProvider<Question, Object> {

  private Scenario scenario;

  public QuestionsProvider(Scenario scenario) {
    this.scenario = scenario;
  }

  @Override
  protected Stream<Question> fetchFromBackEnd(Query<Question, Object> query) {
    return DaoProvider.getDao().getQuestions(scenario).stream();
  }

  @Override
  protected int sizeInBackEnd(Query<Question, Object> query) {
    return DaoProvider.getDao().getQuestionsCount(scenario);
  }
}
