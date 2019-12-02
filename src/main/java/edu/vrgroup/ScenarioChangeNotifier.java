package edu.vrgroup;

import edu.vrgroup.model.Game;
import edu.vrgroup.model.Scenario;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ScenarioChangeNotifier {

  private List<ScenarioChangeListener> listeners = new CopyOnWriteArrayList<>();
  private Scenario scenario;

  public void registerListener(ScenarioChangeListener listener) {
    listeners.add(listener);
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
    for (ScenarioChangeListener l : listeners) {
      l.scenarioChanged(scenario);
    }
  }

  public Scenario getScenario() {
    return scenario;
  }
}
