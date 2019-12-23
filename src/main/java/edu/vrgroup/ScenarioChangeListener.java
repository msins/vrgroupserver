package edu.vrgroup;

import com.vaadin.flow.server.VaadinSession;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Scenario;

public interface ScenarioChangeListener {

  void scenarioChanged(Scenario scenario);

  default void registerToScenarioNotifier(Game game) {
    if (game == null) {
      return;
    }
    String notifierAttribute = game.getName() + ".scenario.notifier";
    ScenarioChangeNotifier notifier = (ScenarioChangeNotifier) VaadinSession.getCurrent()
        .getAttribute(notifierAttribute);
    if (notifier == null) {
      notifier = new ScenarioChangeNotifier();
      VaadinSession.getCurrent().setAttribute(notifierAttribute, notifier);
    }
    notifier.registerListener(this);
    if (notifier.getScenario() != null) {
      scenarioChanged(notifier.getScenario());
    }
  }

  default void unregisterFromScenarioNotifier(Game game) {
    if (game == null) {
      return;
    }
    String notifierAttribute = game.getName() + ".scenario.notifier";
    ScenarioChangeNotifier notifier = (ScenarioChangeNotifier) VaadinSession.getCurrent()
        .getAttribute(notifierAttribute);
    if (notifier != null) {
      notifier.unregisterListener(this);
    }
  }
}
