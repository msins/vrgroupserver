package edu.vrgroup.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

@Entity(name = "Choice")
public class Choice implements Serializable {

  @Id
  @Column(name = "questionId")
  @JoinColumn(name = "questionId")
  private Integer questionId;

  @Id
  @Column(name = "text")
  private String text;

  public Choice() {

  }

  public Choice(Integer questionId, String text) {
    this.questionId = questionId;
    this.text = text;
  }

  public Integer getQuestionId() {
    return questionId;
  }

  public String getText() {
    return text;
  }

  @Override
  public String toString() {
    return text;
  }
}
