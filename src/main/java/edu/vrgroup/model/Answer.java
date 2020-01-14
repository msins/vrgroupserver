package edu.vrgroup.model;

import com.google.common.base.MoreObjects;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Answer implements Serializable, Comparable<Answer> {

  @Id
  @Column(name = "timestampCreated")
  private Timestamp timestamp;

  @Id
  @ManyToOne(targetEntity = Question.class)
  @JoinColumn(name = "questionId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (questionId) REFERENCES Question(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Question question;

  @Id
  @ManyToOne(targetEntity = Scenario.class)
  @JoinColumn(name = "scenarioId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (scenarioId) REFERENCES Scenario(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Scenario scenario;

  @Id
  @ManyToOne
  @JoinColumn(name = "gameId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (gameId) REFERENCES Game(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Game game;

  @Id
  @ManyToOne
  @JoinColumn(name = "userId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (userId) REFERENCES User(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private User user;

  @Id
  @ManyToOne
  @JoinColumn(name = "choiceId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (choiceId) REFERENCES Choice(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Choice choice;

  @Column
  private String IP;

  public Answer() {
  }

  public Answer(Timestamp timestamp, Question question, Scenario scenario, Game game, User user, Choice choice,
      String IP) {
    this.timestamp = timestamp;
    this.question = question;
    this.scenario = scenario;
    this.game = game;
    this.user = user;
    this.choice = choice;
    this.IP = IP;
  }

  public Timestamp getTimestamp() {
    return timestamp;
  }

  public void setTimestamp(Timestamp timestamp) {
    this.timestamp = timestamp;
  }

  public Question getQuestion() {
    return question;
  }

  public void setQuestion(Question question) {
    this.question = question;
  }

  public Scenario getScenario() {
    return scenario;
  }

  public void setScenario(Scenario scenario) {
    this.scenario = scenario;
  }

  public Game getGame() {
    return game;
  }

  public void setGame(Game game) {
    this.game = game;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public Choice getChoice() {
    return choice;
  }

  public void setChoice(Choice choice) {
    this.choice = choice;
  }

  public String getIP() {
    return IP;
  }

  public void setIP(String IP) {
    this.IP = IP;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("timestamp", timestamp)
        .add("question", question)
        .add("scenario", scenario)
        .add("game", game)
        .add("choice", choice)
        .add("IPv4", IP)
        .toString();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Answer answer = (Answer) o;
    return timestamp.equals(answer.timestamp) &&
        question.equals(answer.question) &&
        scenario.equals(answer.scenario) &&
        game.equals(answer.game) &&
        user.equals(answer.user) &&
        choice.equals(answer.choice);
  }

  @Override
  public int hashCode() {
    return Objects.hash(timestamp, question, scenario, game, user, choice);
  }

  @Override
  public int compareTo(Answer other) {
    return this.timestamp.compareTo(other.timestamp);
  }
}
