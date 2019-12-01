package edu.vrgroup;

import edu.vrgroup.model.Game;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class GameChangeNotifier {

  private List<GameChangeListener> listeners = new CopyOnWriteArrayList<>();
  private Game game;

  public void registerListener(GameChangeListener listener) {
    listeners.add(listener);
  }

  public void setGame(Game game) {
    this.game = game;
    for (GameChangeListener l : listeners) {
      l.gameChanged(game);
    }
  }

  public Game getGame() {
    return game;
  }
}
