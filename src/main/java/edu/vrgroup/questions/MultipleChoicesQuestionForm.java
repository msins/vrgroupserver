package edu.vrgroup.questions;

import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.textfield.TextField;

public class MultipleChoicesQuestionForm extends FormLayout {

  public MultipleChoicesQuestionForm() {
    ListBox<TextField> choices = new ListBox<>();
    add(choices);
  }
}
