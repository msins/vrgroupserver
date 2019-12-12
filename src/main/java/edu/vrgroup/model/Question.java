package edu.vrgroup.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Question")
public class Question implements Serializable, Comparable<Question> {

  @Id
  @Column(name = "id")
  @Expose
  protected Integer id;

  @Column
  @Expose
  protected String text;

  @OneToMany
  @JoinColumn(name = "questionId")
  @Expose
  private List<Choice> choices;

  @Enumerated(EnumType.ORDINAL)
  @Column
  @Expose
  private Type type;

  public Question() {
  }

  public Question(String text, Type type) {
    this.text = text;
    this.id = text.hashCode() ^ ThreadLocalRandom.current().nextInt();
    this.type = type;
  }

  public Integer getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  public Type getType() {
    return type;
  }

  public void setChoices(List<Choice> choices) {
    this.choices = choices;
  }

  /**
   * Used to render answers in dashboard
   */
  public List<Choice> getChoices() {
    return choices;
  }

  public Question apply(String newText, Type type) {
    Question newQuestion = new Question(newText, type);
    List<Choice> newChoices = this.choices.stream()
        .map(old -> new Choice(newQuestion, old.getValue(), old.getOrder()))
        .sorted()
        .collect(Collectors.toList());
    newQuestion.setChoices(newChoices);
    return newQuestion;
  }

  public enum Type {
    MULTIPLE_CHOICE("Multiple choices"),
    SCALING_QUESTION("Scaling question");

    @Column(name = "name")
    private String name;

    Type(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }

    @Override
    public String toString() {
      return name;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Question question = (Question) o;
    return id.equals(question.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public int compareTo(Question other) {
    return this.text.compareTo(other.text);
  }

}
