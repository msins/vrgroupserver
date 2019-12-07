package edu.vrgroup.model;

import com.google.gson.annotations.Expose;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "Question")
public class Question {

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

  public Question() {
  }

  public Question(String text) {
    this.text = text;
    this.id = text.hashCode() ^ ThreadLocalRandom.current().nextInt();
  }

  public Integer getId() {
    return id;
  }

  public String getText() {
    return text;
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

  public Question apply(String newText) {
    Question newQuestion = new Question(newText);
    List<Choice> newChoices = this.choices.stream()
        .map(old -> new Choice(newQuestion, old.getValue(), old.getOrder()))
        .collect(Collectors.toList());
    newQuestion.setChoices(newChoices);
    return newQuestion;
  }

  public enum Type {
    MULTIPLE_CHOICE("Multiple choices");

    private String name;

    Type(String name) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}
