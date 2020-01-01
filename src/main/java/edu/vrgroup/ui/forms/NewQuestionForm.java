package edu.vrgroup.ui.forms;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.TextRenderer;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Question.Type;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.ui.forms.MultipleChoicesQuestionForm.IndexedChoice;
import edu.vrgroup.ui.providers.ExistingQuestionProvider;
import edu.vrgroup.ui.util.AbstractButtonFactory;
import edu.vrgroup.ui.util.StylizedVerticalLayout;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class NewQuestionForm extends Dialog {

  private VerticalLayout layout = new VerticalLayout();
  private VerticalLayout form;
  Select<Question.Type> select;

  public NewQuestionForm(Scenario scenario, Consumer<Question> onCreate) {
    select = new Select<>(Type.values());
    select.setPlaceholder("Pick question type");
    select.setItemLabelGenerator(Question.Type::getName);
    select.addValueChangeListener(e -> replaceLayout(new MultipleChoicesQuestionForm()));

    Button createBtn = AbstractButtonFactory.getRectangle().createPrimaryButton("Create", e -> {
      if (scenario == null) {
        new Notification("Select a scenario", 3000).open();
        return;
      }

      MultipleChoicesQuestionForm multipleForm = (MultipleChoicesQuestionForm) form;

      try {
        findErrorsAndExecuteTheirProcedures(multipleForm);
        return;
      } catch (NoSuchElementException ignored) {
      }

      Question newQuestion = extractQuestion(multipleForm);

      DaoProvider.getDao().addQuestion(scenario, newQuestion);
      onCreate.accept(newQuestion);
      close();
    });
    createBtn.addClickShortcut(Key.ENTER);

    Button closeBtn = AbstractButtonFactory.getRectangle().createRedButton("Cancel", e -> close());

    Button fromExistingQuestion = AbstractButtonFactory.getCircular()
        .createGreenButton(VaadinIcon.DATABASE.create(), this::onFromExistingQuestionButtonClicked);

    add(new HorizontalLayout(select, createBtn, closeBtn, fromExistingQuestion), layout);
  }

  private void replaceLayout(VerticalLayout newLayout) {
    layout.removeAll();
    form = newLayout;
    layout.add(form);
  }

  private void findErrorsAndExecuteTheirProcedures(MultipleChoicesQuestionForm form) {
    Stream.of(
        checkForDuplicates(form),
        checkForEmptyQuestionText(form),
        checkForEmptyChoiceFields(form)
    )
        .filter(Objects::nonNull)
        .filter(ImmutablePair::getLeft)
        .map(ImmutablePair::getRight)
        .reduce(Consumer::andThen)
        .ifPresentOrElse(errorProcedure -> errorProcedure.accept(null), () -> {
          throw new NoSuchElementException();
        });
  }

  private ImmutablePair<Boolean, Consumer<Void>> checkForDuplicates(MultipleChoicesQuestionForm form) {
    List<TextField> duplicates = form.getChoiceFields().getFields().stream()
        .filter(i -> Collections.frequency(form.getChoiceFields().getFields(), i) > 1)
        .collect(Collectors.toList());

    if (duplicates.size() != 0) {
      return ImmutablePair.of(true, onErrorProcedure -> {
        duplicates.forEach(textField -> textField.setErrorMessage("Duplicate choice"));
        duplicates.forEach(d -> d.setInvalid(true));
      });
    }
    return null;
  }

  private ImmutablePair<Boolean, Consumer<Void>> checkForEmptyQuestionText(MultipleChoicesQuestionForm form) {
    if (form.getText().getValue().trim().isEmpty()) {
      return ImmutablePair.of(true, onErrorProcedure -> {
        form.getText().setErrorMessage("Required");
        form.getText().setInvalid(true);
      });
    }
    return null;
  }

  private ImmutablePair<Boolean, Consumer<Void>> checkForEmptyChoiceFields(MultipleChoicesQuestionForm form) {
    List<TextField> fields = form.getChoiceFields().getFields();
    if (fields.stream().filter(field -> !field.isEmpty()).count() < 2) {
      return ImmutablePair.of(true, onErrorProcedure -> {
        for (int i = 0; i < 2; i++) {
          if (fields.get(i).isEmpty()) {
            fields.get(i).setErrorMessage("Required");
            fields.get(i).setInvalid(true);
          }
        }
      });
    }
    return null;
  }

  private Question extractQuestion(MultipleChoicesQuestionForm form) {
    IndexedChoice[] indexedChoices = form.getChoicesValues();
    Question newQuestion = new Question(form.getText().getValue(), select.getValue());

    List<Choice> choices = Arrays.stream(indexedChoices)
        .filter(c -> !c.getValue().trim().isEmpty())
        .map(c -> new Choice(newQuestion, c.getValue(), c.getIndex()))
        .sorted()
        .collect(Collectors.toList());
    newQuestion.setChoices(choices);
    return newQuestion;
  }

  private void onFromExistingQuestionButtonClicked(ClickEvent<Button> e) {
    replaceLayout(new FromExistingQuestionForm(question -> {
      select.setValue(question.getType());
      replaceLayout(new MultipleChoicesQuestionForm(question.getText(),
          question.getChoices().stream().map(Choice::getValue).collect(Collectors.toList())));
    }));
  }

  private static class FromExistingQuestionForm extends StylizedVerticalLayout {

    public FromExistingQuestionForm(Consumer<Question> onPick) {
      ListBox<Question> questionList = new ListBox<>();
      questionList.setRenderer(new TextRenderer<>(Question::getText));
      questionList.setWidthFull();

      questionList.setDataProvider(new ExistingQuestionProvider());
      questionList.addValueChangeListener(e -> onPick.accept(e.getValue()));

      add(questionList);
    }
  }

}
