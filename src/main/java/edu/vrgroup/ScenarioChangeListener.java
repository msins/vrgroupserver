package edu.vrgroup;

import edu.vrgroup.model.Scenario;

public interface ScenarioChangeListener {

  void scenarioChanged(Scenario scenario);

  void registerToScenarioNotifier();

}
