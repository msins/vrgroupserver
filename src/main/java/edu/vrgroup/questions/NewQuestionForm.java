package edu.vrgroup.questions;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import edu.vrgroup.model.Question;
import edu.vrgroup.ui.util.ButtonFactory;

public class NewQuestionForm extends Dialog {

  public NewQuestionForm() {
    Select<Question.Type> select = new Select<>(Question.Type.values());
    select.setPlaceholder("Pick question type");
    select.setItemLabelGenerator(Question.Type::getName);

    select.addValueChangeListener(e -> {

    });

    Button createBtn = ButtonFactory.createPrimaryButton("Create", e -> {

      close();
    });

    Button closeBtn = ButtonFactory.createRedButton("Cancel", e -> close());

    add(new HorizontalLayout(select, createBtn, closeBtn));
  }
}
