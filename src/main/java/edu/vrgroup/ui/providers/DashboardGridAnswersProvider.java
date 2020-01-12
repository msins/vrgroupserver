package edu.vrgroup.ui.providers;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import edu.vrgroup.database.Dao;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Scenario;
import java.util.stream.Stream;

public class DashboardGridAnswersProvider extends AbstractBackEndDataProvider<Answer, Object> {

  private Game game;
  private Scenario scenario;

  public DashboardGridAnswersProvider() {
  }

  public DashboardGridAnswersProvider(Game game) {
    this.game = game;
  }

  public DashboardGridAnswersProvider(Game game, Scenario scenario) {
    this.game = game;
    this.scenario = scenario;
  }

  @Override
  protected Stream<Answer> fetchFromBackEnd(Query<Answer, Object> query) {
    //temporary design
    if (scenario != null && game != null) {
      return DaoProvider.getDao().getAnswers(game, scenario, query.getOffset(), query.getLimit());
    }
    if (game != null) {
      return DaoProvider.getDao().getAnswers(game, query.getOffset(), query.getLimit());
    }

    return DaoProvider.getDao().getAnswers(query.getOffset(), query.getLimit());
  }

  @Override
  protected int sizeInBackEnd(Query<Answer, Object> query) {
    //temporary design
    if (scenario != null && game != null) {
      return DaoProvider.getDao().getAnswersCount(game, scenario);
    }
    if (game != null) {
      return DaoProvider.getDao().getAnswersCount(game);
    }

    return DaoProvider.getDao().getAnswersCount();
  }
}
