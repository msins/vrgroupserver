package edu.vrgroup.ui.forms;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import edu.vrgroup.ui.util.AbstractButtonFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class MultipleChoicesQuestionForm extends VerticalLayout {

  private TextArea text = new TextArea("Text");
  private ChoiceFields choiceFields = new ChoiceFields();
  Button newChoice;

  public MultipleChoicesQuestionForm() {
    initUi();
    //needs to have 2 choices minimum
    newChoice.click();
    newChoice.click();
  }

  public MultipleChoicesQuestionForm(String text, List<String> choices) {
    initUi();
    this.text.setValue(text);
    choices.forEach(
        choice -> choiceFields
            .add(new DistinctTextField("", choice, String.valueOf(choiceFields.getFields().size() + 1))));
  }

  private void initUi() {
    newChoice = AbstractButtonFactory.getCircular().createPrimaryButton(VaadinIcon.PLUS.create(), e -> {
      TextField field = new DistinctTextField("", String.valueOf(choiceFields.getFields().size() + 1));
      choiceFields.add(field);
    });
    text.setWidthFull();

    setAlignItems(Alignment.CENTER);
    add(text, choiceFields, newChoice);
  }

  private static class DistinctTextField extends TextField {

    {
      setWidthFull();
    }

    public DistinctTextField(String label, String initialValue, String placeholder) {
      super(label, initialValue, placeholder);
    }

    public DistinctTextField(String label, String placeholder) {
      super(label, placeholder);
    }

    @Override
    public boolean equals(Object obj) {
      if (!(obj instanceof TextField)) {
        return false;
      }
      return this.getValue().equalsIgnoreCase(((TextField) obj).getValue());
    }
  }

  public TextArea getText() {
    return text;
  }

  public ChoiceFields getChoiceFields() {
    return choiceFields;
  }

  public IndexedChoice[] getChoicesValues() {
    return IntStream.range(0, choiceFields.getFields().size())
        .mapToObj(i -> new IndexedChoice(i, choiceFields.getFields().get(i).getValue()))
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

  public static class ChoiceFields extends VerticalLayout {

    private List<TextField> fields = new ArrayList<>();

    {
      initStyle();
    }

    public void add(TextField textField) {
      super.add(textField);
      fields.add(textField);
    }

    public List<TextField> getFields() {
      return fields;
    }

    private void initStyle() {
      getElement().getStyle().set("border-style", "groove");
      getElement().getStyle().set("border-width", "thin");
      getElement().getStyle().set("border-color", "var(--lumo-primary-color-10pct)");
      getElement().getStyle().set("border-radius", "var(--lumo-border-radius-m)");
      getElement().getStyle().set("box-shadow", "var(--lumo-boc-shadow-s)");
      setAlignItems(Alignment.CENTER);
    }
  }
}
