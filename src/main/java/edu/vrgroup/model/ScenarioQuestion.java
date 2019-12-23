package edu.vrgroup.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "ScenarioQuestion")
public class ScenarioQuestion implements Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "scenarioId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (scenarioId) REFERENCES Scenario(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Scenario scenario;

  @Id
  @ManyToOne
  @JoinColumn(name = "questionId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (questionId) REFERENCES Question(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Question question;

  public ScenarioQuestion(Scenario scenario, Question question) {
    this.scenario = scenario;
    this.question = question;
  }

  public ScenarioQuestion() {
  }

  public Scenario getScenario() {
    return scenario;
  }

  public Question getQuestion() {
    return question;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ScenarioQuestion that = (ScenarioQuestion) o;
    return scenario.equals(that.scenario) &&
        question.equals(that.question);
  }

  @Override
  public int hashCode() {
    return Objects.hash(scenario, question);
  }
}
