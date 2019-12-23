package edu.vrgroup.ui.forms;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Question.Type;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.ui.forms.MultipleChoicesQuestionForm.IndexedChoice;
import edu.vrgroup.ui.util.AbstractButtonFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NewQuestionForm extends Dialog {

  private VerticalLayout layout;
  private VerticalLayout form;

  public NewQuestionForm(Scenario scenario, Consumer<Question> onCreate) {
    Select<Question.Type> select = new Select<>(Type.values());
    select.setPlaceholder("Pick question type");
    select.setItemLabelGenerator(Question.Type::getName);

    layout = new VerticalLayout();

    select.addValueChangeListener(e -> {
      if (select.getValue() == Type.MULTIPLE_CHOICE) {
        layout.removeAll();
        form = new MultipleChoicesQuestionForm();
        layout.add(form);
      } else if (select.getValue() == Type.SCALING_QUESTION) {
        layout.removeAll();
        form = new MultipleChoicesQuestionForm();
        layout.add(form);
      }
    });

    Button createBtn = AbstractButtonFactory.getRectangle().createPrimaryButton("Create", e -> {
      if (scenario == null) {
        Notification notification = new Notification("Select a scenario", 3000);
        notification.getElement().getStyle().set("color", "red");
        notification.open();
        return;
      }

      MultipleChoicesQuestionForm f = ((MultipleChoicesQuestionForm) form);

        boolean hasError = false;
        if (f.getText().getValue().trim().isEmpty()) {
          f.getText().setErrorMessage("Required");
          f.getText().setInvalid(true);
          hasError = true;
        }


        List<TextField> fields = f.getChoices().getFields();
        List<TextField> duplicates = fields.stream().filter(i -> Collections.frequency(fields, i) > 1)
            .collect(Collectors.toList());
        if (duplicates.size() != 0) {
          hasError = true;
          duplicates.forEach(d -> d.setErrorMessage("Duplicate choice"));
          duplicates.forEach(d -> d.setInvalid(true));
        }

        if (fields.stream().filter(field -> !field.isEmpty()).count() < 2) {
          for (int i = 0; i < 2; i++) {
            if (fields.get(i).isEmpty()) {
              fields.get(i).setErrorMessage("Required");
              fields.get(i).setInvalid(true);
              hasError = true;
            }
          }
        }

        if (!hasError) {
          IndexedChoice[] indexedChoices = f.getChoicesValues();
          Question newQuestion = new Question(f.getText().getValue(), select.getValue());
          List<Choice> choices = Arrays.stream(indexedChoices)
              .map(c -> new Choice(newQuestion, c.getValue(), c.getIndex()))
              .sorted()
              .collect(Collectors.toList());
          newQuestion.setChoices(choices);
          DaoProvider.getDao().addQuestion(scenario, newQuestion);
          onCreate.accept(newQuestion);
          close();
        }
    });
    createBtn.addClickShortcut(Key.ENTER);

    Button closeBtn = AbstractButtonFactory.getRectangle().createRedButton("Cancel", e -> close());

    add(new HorizontalLayout(select, createBtn, closeBtn), layout);
  }
}
