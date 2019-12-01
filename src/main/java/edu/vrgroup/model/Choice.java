package edu.vrgroup.model;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Choice")
public class Choice implements Serializable {

  @Id
  @Column(name = "questionId")
  private Integer questionId;

  @Id
  @Column(name = "text")
  private String text;

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
