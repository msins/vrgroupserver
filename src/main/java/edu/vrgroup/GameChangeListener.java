package edu.vrgroup;

import com.vaadin.flow.server.VaadinSession;
import edu.vrgroup.model.Game;

public interface GameChangeListener {

  void gameChanged(Game game);

  default void registerToGameNotifier() {
    GameChangeNotifier notifier = (GameChangeNotifier) VaadinSession.getCurrent().getAttribute("game.notifier");
    if (notifier == null) {
      notifier = new GameChangeNotifier();
      VaadinSession.getCurrent().setAttribute("game.notifier", notifier);
    }
    notifier.registerListener(this);
    if (notifier.getGame() != null) {
      gameChanged(notifier.getGame());
    }
  }

  default void unregisterFromGameNotifier() {
    GameChangeNotifier notifier = (GameChangeNotifier) VaadinSession.getCurrent().getAttribute("game.notifier");
    if (notifier != null) {
      notifier.unregisterListener(this);
    }
  }
}
