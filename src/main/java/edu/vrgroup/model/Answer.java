package edu.vrgroup.model;

import com.google.common.base.MoreObjects;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.database.JpaEntityManagerFactoryProvider;
import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Persistence;

@Entity
public class Answer<T extends Question> implements Serializable {

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("vr.server.db");
    JpaEntityManagerFactoryProvider.setEmf(emf);
    System.out.println(DaoProvider.getDao().getAnswers(0, 100));
    System.out.println(DaoProvider.getDao().getAnswersCount());
    JpaEntityManagerFactoryProvider.setEmf(null);
    if (emf != null) {
      emf.close();
    }
  }

  @Id
  @Column(name = "timestampCreated")
  private Timestamp timestamp;

  @Id
  @ManyToOne(targetEntity = Question.class)
  @JoinColumn(name = "questionId")
  private T question;

  @Id
  @ManyToOne(targetEntity = Scenario.class)
  @JoinColumn(name = "scenarioId")
  private Scenario scenario;

  @Id
  @ManyToOne
  @JoinColumn(name = "gameId")
  private Game game;

  @Id
  @ManyToOne
  @JoinColumn(name = "userId")
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
