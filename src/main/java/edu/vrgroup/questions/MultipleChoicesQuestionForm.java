package edu.vrgroup.questions;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import javax.persistence.criteria.CriteriaBuilder.In;

public class MultipleChoicesQuestionForm extends VerticalLayout {

  private TextArea text = new TextArea("Text");
  private Choices choices = new Choices();

  public MultipleChoicesQuestionForm() {
    Button newChoice = new Button(VaadinIcon.PLUS.create(), e -> {
      TextField field = new TextField("", String.valueOf(choices.getFields().size() + 1)) {
        @Override
        public boolean equals(Object obj) {
          if (!(obj instanceof TextField)) {
            return false;
          }
          return this.getValue().equalsIgnoreCase(((TextField) obj).getValue());
        }
      };
      field.setWidthFull();
      choices.add(field);
    });
    text.setWidthFull();
    newChoice.getStyle().set("border-radius", "50%");

    //needs to have 2 choices minimum
    newChoice.click();
    newChoice.click();

    setAlignItems(Alignment.CENTER);
    add(text, choices, newChoice);
  }

  public TextArea getText() {
    return text;
  }

  public Choices getChoices() {
    return choices;
  }

  public IndexedChoice[] getChoicesValues() {
    return IntStream.range(0, choices.getFields().size())
        .mapToObj(i -> new IndexedChoice(i, choices.getFields().get(i).getValue()))
        .toArray(IndexedChoice[]::new);
  }

  public static class IndexedChoice {

    private int index;
    private String value;

    public IndexedChoice(int index, String value) {
      this.index = index;
      this.value = value;
    }

    public int getIndex() {
      return index;
    }

    public String getValue() {
      return value;
    }
  }

  public static class Choices extends VerticalLayout {

    private List<TextField> fields = new ArrayList<>();

    public Choices() {
      getElement().getStyle().set("border-style", "groove");
      getElement().getStyle().set("border-width", "thin");
      getElement().getStyle().set("border-color", "var(--lumo-primary-color-10pct)");
      getElement().getStyle().set("border-radius", "var(--lumo-border-radius-m)");
      getElement().getStyle().set("box-shadow", "var(--lumo-boc-shadow-s)");
      setAlignItems(Alignment.CENTER);
    }

    public void add(TextField textField) {
      super.add(textField);
      fields.add(textField);
    }

    public List<TextField> getFields() {
      return fields;
    }
  }
}
