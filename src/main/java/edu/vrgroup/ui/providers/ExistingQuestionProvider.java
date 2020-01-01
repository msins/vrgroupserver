package edu.vrgroup.ui.providers;

import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Question;
import java.util.Objects;
import java.util.stream.Stream;

public class ExistingQuestionProvider extends AbstractBackEndDataProvider<Question, Object> {

  @Override
  protected Stream<Question> fetchFromBackEnd(Query<Question, Object> query) {
    return DaoProvider.getDao().getAllQuestions()
        .map(SchwartzianQuestionTransform::new)
        .distinct()
        .map(SchwartzianQuestionTransform::getQuestion);
  }

  @Override
  protected int sizeInBackEnd(Query<Question, Object> query) {
    return DaoProvider.getDao().getQuestionCount();
  }

  private static class SchwartzianQuestionTransform {

    Question question;

    public SchwartzianQuestionTransform(Question question) {
      this.question = question;
    }

    public Question getQuestion() {
      return question;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      SchwartzianQuestionTransform that = (SchwartzianQuestionTransform) o;
      return Objects.equals(question.getText(), that.question.getText());
    }

    @Override
    public int hashCode() {
      return Objects.hash(question.getText());
    }
  }
}
