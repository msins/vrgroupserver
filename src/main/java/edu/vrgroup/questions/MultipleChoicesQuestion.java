package edu.vrgroup.questions;

import com.google.common.base.MoreObjects;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Question;
import edu.vrgroup.database.JpaEntityManagerFactoryProvider;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.EntityManagerFactory;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Persistence;
import javax.persistence.PrimaryKeyJoinColumn;

@Entity(name = "MultipleChoicesQuestion")
@PrimaryKeyJoinColumn(name = "questionId")
public class MultipleChoicesQuestion extends Question {

  @OneToMany
  @JoinColumn(name = "questionId")
  private List<Choice> choices;

  public MultipleChoicesQuestion(String text) {
    super(text, Type.MULTIPLE_CHOICES);
  }

  public MultipleChoicesQuestion() {
  }

  public static void main(String[] args) {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("vr.server.db");
    JpaEntityManagerFactoryProvider.setEmf(emf);

    JpaEntityManagerFactoryProvider.setEmf(null);
    if (emf != null) {
      emf.close();
    }
  }

  @Override
  public List<Choice> getChoices() {
    return choices;
  }

  public void setChoices(List<Choice> choices) {
    this.choices = choices;
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("text", text)
        .add("type", type)
        .add("choices", choices)
        .toString();
  }
}
