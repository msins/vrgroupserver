package edu.vrgroup.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "Choice")
public class Choice implements Serializable {

  @Id
  @ManyToOne
  @JoinColumn(name = "questionId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (questionId) REFERENCES Question(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Question question;

  @Id
  @Column(name = "text")
  @Expose
  private String value;

  public Choice() {

  }

  public Choice(Question question, String value) {
    this.question = question;
    this.value = value;
  }

  public Question getQuestion() {
    return question;
  }

  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return value;
  }
}
