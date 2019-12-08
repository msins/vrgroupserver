package edu.vrgroup.model;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "GameScenario")
public class GameScenario implements Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "gameId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (gameId) REFERENCES Game(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Game game;

  @Id
  @ManyToOne
  @JoinColumn(name = "scenarioId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (scenarioId) REFERENCES Scenario(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Scenario scenario;

  public GameScenario(Game game, Scenario scenario) {
    this.game = game;
    this.scenario = scenario;
  }

  public GameScenario() {

  }

  public Game getGame() {
    return game;
  }

  public Scenario getScenario() {
    return scenario;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    GameScenario that = (GameScenario) o;
    return game.equals(that.game) &&
        scenario.equals(that.scenario);
  }

  @Override
  public int hashCode() {
    return Objects.hash(game, scenario);
  }
}
