package edu.vrgroup.model;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Table(name = "Question")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Question {

  @Id
  @Column(name = "id")
  protected Integer id;

  @Column
  protected String text;

  @Column
  protected Integer type;

  public Question() {
  }

  public Question(String text, Type type) {
    this.text = text;
    this.type = type.intValue();
    this.id = text.hashCode();
  }

  public Integer getId() {
    return id;
  }

  public String getText() {
    return text;
  }

  /**
   * Used to render answers in dashboard
   */
  public abstract List<Choice> getChoices();

  public enum Type {
    MULTIPLE_CHOICES(1, "Multiple choices"),
    SCALING(2, "Scaling");

    int type;
    String name;

    Type(int type, String name) {
      this.type = type;
      this.name = name;
    }

    public int intValue() {
      return type;
    }

    public String getName() {
      return name;
    }

  }

}
