package edu.vrgroup.questions;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.ui.util.ButtonFactory;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class NewQuestionForm extends Dialog {

  private VerticalLayout layout;
  private VerticalLayout form;

  public NewQuestionForm(Game game, Consumer<Question> onCreate) {
    Select<Question.Type> select = new Select<>(Question.Type.values());
    select.setPlaceholder("Pick question type");
    select.setItemLabelGenerator(Question.Type::getName);

    layout = new VerticalLayout();

    select.addValueChangeListener(e -> {
      layout.removeAll();
      switch (e.getValue()) {
        case MULTIPLE_CHOICES:
          form = new MultipleChoicesQuestionForm();
          layout.add(form);
          break;
        case SCALING:
      }
    });

    Button createBtn = ButtonFactory.createPrimaryButton("Create", e -> {
      if (game == null) {
        Notification notification = new Notification("Select a game", 3000);
        notification.getElement().getStyle().set("color", "red");
        notification.open();
        return;
      }

      //todo add other question types?
      switch (select.getValue()) {
        case MULTIPLE_CHOICES:
          MultipleChoicesQuestionForm f = ((MultipleChoicesQuestionForm) form);

          boolean hasError = false;
          if (f.getText().getValue().trim().isEmpty()) {
            f.getText().setInvalid(true);
            hasError = true;
          }

          List<TextField> fields = f.getChoices().getFields();
          if (fields.stream().filter(field -> !field.isEmpty()).count() < 2) {
            for (int i = 0; i < 2; i++) {
              if (fields.get(i).isEmpty()) {
                fields.get(i).setInvalid(true);
                hasError = true;
              }
            }
          }

          List<TextField> duplicates = fields.stream().filter(i -> Collections.frequency(fields, i) > 1)
              .collect(Collectors.toList());
          if (duplicates.size() != 0) {
            hasError = true;
            duplicates.forEach(d -> d.setInvalid(true));
          }
          if (!hasError) {
            String[] values = f.getChoicesValues();
            MultipleChoicesQuestion newQuestion = new MultipleChoicesQuestion(f.getText().getValue());
            List<Choice> choices = Arrays.stream(values)
                .map(value -> new Choice(newQuestion, value))
                .collect(Collectors.toList());
            newQuestion.setChoices(choices);
            DaoProvider.getDao().addQuestion(game, newQuestion);
            onCreate.accept(newQuestion);
            close();
          }
          break;
      }
    });

    Button closeBtn = ButtonFactory.createRedButton("Cancel", e -> close());

    add(new HorizontalLayout(select, createBtn, closeBtn), layout);
  }
}
