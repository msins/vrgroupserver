package edu.vrgroup.model;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "GameQuestion")
public class GameQuestion implements Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "gameId")
  private Game game;

  @Id
  @ManyToOne
  @JoinColumn(name = "questionId")
  private Question question;

  public GameQuestion(Game game, Question question) {
    this.game = game;
    this.question = question;
  }

  public GameQuestion() {
  }

  public Game getGame() {
    return game;
  }

  public Question getQuestion() {
    return question;
  }
}
