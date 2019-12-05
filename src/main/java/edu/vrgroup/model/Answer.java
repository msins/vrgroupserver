package edu.vrgroup.model;

import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Answer<T extends Question> implements Serializable {

  @Id
  @Column(name = "timestampCreated")
  private Timestamp timestamp;

  @Id
  @ManyToOne(targetEntity = Question.class)
  @JoinColumn(name = "questionId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (questionId) REFERENCES Question(id) ON DELETE CASCADE"
  ))
  private T question;

  @Id
  @ManyToOne(targetEntity = Scenario.class)
  @JoinColumn(name = "scenarioId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (scenarioId) REFERENCES Scenario(id) ON DELETE CASCADE"
  ))
  private Scenario scenario;

  @Id
  @ManyToOne
  @JoinColumn(name = "gameId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (gameId) REFERENCES Game(id) ON DELETE CASCADE"
  ))
  private Game game;

  @Id
  @ManyToOne
  @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE"
  ))
  private User user;

  @Column
  private Integer score;

  @Column
  private String IPv4;

  public Answer() {
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public T getQuestion() {
    return question;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public Game getGame() {
    return game;
  }

  public Integer getScore() {
    return score;
  }

  public String getIPv4() {
    return IPv4;
  }

  public User getUser() {
    return user;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("timestamp", timestamp)
        .add("question", question)
        .add("scenario", scenario)
        .add("game", game)
        .add("score", score)
        .add("IPv4", IPv4)
        .toString();
  }
}
