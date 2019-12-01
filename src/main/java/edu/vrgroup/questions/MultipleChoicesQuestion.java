package edu.vrgroup.questions;

import com.google.common.base.MoreObjects;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Question;
import edu.vrgroup.database.DaoProvider;
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
  public String[] getChoices() {
    return choices.stream().map(Choice::getText).toArray(String[]::new);
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
