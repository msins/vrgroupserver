package edu.vrgroup.ui.providers;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import java.util.stream.Stream;

public class GameDataProvider extends AbstractBackEndDataProvider<Game, Object> {

  @Override
  protected Stream<Game> fetchFromBackEnd(Query<Game, Object> query) {
    return DaoProvider.getDao().getGames();
  }

  @Override
  protected int sizeInBackEnd(Query<Game, Object> query) {
    return DaoProvider.getDao().getGamesCount();
  }

}
