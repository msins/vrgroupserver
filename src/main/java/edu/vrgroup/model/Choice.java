package edu.vrgroup.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.criteria.CriteriaBuilder.In;

@Entity(name = "Choice")
public class Choice implements Serializable, Comparable<Choice> {

  @Id
  @Column(name = "id")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "questionId", nullable = false, foreignKey = @ForeignKey(
      foreignKeyDefinition = "FOREIGN KEY (questionId) REFERENCES Question(id) ON DELETE CASCADE ON UPDATE CASCADE"
  ))
  private Question question;

  @Column(name = "text")
  @Expose
  private String value;

  @Column(name = "orderValue")
  @Expose
  private Integer order;

  public Choice() {

  }

  public Choice(Question question, String value, Integer order) {
    this.id = value.hashCode() ^ ThreadLocalRandom.current().nextInt();
    this.question = question;
    this.value = value;
    this.order = order;
  }

  public Integer getId() {
    return id;
  }

  public Integer getOrder() {
    return order;
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

  @Override
  public int compareTo(Choice other) {
    return Integer.compare(this.order, other.order);
  }
}
