package edu.vrgroup;

import edu.vrgroup.model.Game;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

public class GameChangeNotifier {

  private Set<GameChangeListener> listeners = new CopyOnWriteArraySet<>();
  private Game game;

  public void registerListener(GameChangeListener listener) {
    listeners.add(listener);
  }

  public void unregisterListener(GameChangeListener listener) {
    listeners.remove(listener);
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
