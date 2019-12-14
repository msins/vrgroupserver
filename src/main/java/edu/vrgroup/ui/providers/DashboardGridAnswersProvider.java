package edu.vrgroup.ui.providers;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Game;
import java.util.stream.Stream;

public class DashboardGridAnswersProvider extends AbstractBackEndDataProvider<Answer, Object> {

  private Game game;

  public DashboardGridAnswersProvider(Game game) {
    this.game = game;
  }

  @Override
  protected Stream<Answer> fetchFromBackEnd(Query<Answer, Object> query) {
    return DaoProvider.getDao().getAnswers(game, query.getOffset(), query.getLimit()).stream();
  }

  @Override
  protected int sizeInBackEnd(Query<Answer, Object> query) {
    return DaoProvider.getDao().getAnswersCount(game);
  }
}
