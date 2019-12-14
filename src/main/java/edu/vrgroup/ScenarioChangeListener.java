package edu.vrgroup;

import edu.vrgroup.model.Scenario;

//while usable and implemented in few ui elements, none of them can trigger scenario change for now.
@Deprecated
public interface ScenarioChangeListener {

  void scenarioChanged(Scenario scenario);

  void registerToScenarioNotifier();
}
