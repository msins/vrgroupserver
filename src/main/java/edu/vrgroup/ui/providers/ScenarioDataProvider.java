package edu.vrgroup.ui.providers;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Scenario;
import java.util.stream.Stream;

public class ScenarioDataProvider extends AbstractBackEndDataProvider<Scenario, Object> {

  private Game game;

  public ScenarioDataProvider(Game game) {
    this.game = game;
  }

  @Override
  protected Stream<Scenario> fetchFromBackEnd(Query<Scenario, Object> query) {
    return DaoProvider.getDao().getScenarios(game).stream();
  }

  @Override
  protected int sizeInBackEnd(Query<Scenario, Object> query) {
    return DaoProvider.getDao().getScenarioCount(game);
  }
}
