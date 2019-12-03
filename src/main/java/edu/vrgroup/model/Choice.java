package edu.vrgroup.model;

import com.google.gson.annotations.Expose;
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
  @Expose
  private String value;

  public Choice() {

  }

  public Choice(Integer questionId, String value) {
    this.questionId = questionId;
    this.value = value;
  }

  public Integer getQuestionId() {
    return questionId;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}
