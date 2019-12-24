package edu.vrgroup.ui.util;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.select.Select;

public class DynamicSelect<T> extends StylizedFlexLayout {

  private Button add;
  private Button remove;
  private Select<T> select;

  public DynamicSelect(DynamicSelectListener<T> l) {
    select = new Select<>();
    select.setSizeUndefined();
    select.addValueChangeListener(e -> l.onSelectionChange(e.getValue()));

    add = AbstractButtonFactory.getCircular()
        .createGreenButton(VaadinIcon.PLUS.create(), e -> l.onAdd(select.getValue()));
    add.addThemeVariants(ButtonVariant.LUMO_SMALL);

    remove = AbstractButtonFactory.getCircular()
        .createRedButton(VaadinIcon.MINUS.create(), e -> l.onRemove(select.getValue()));
    remove.addThemeVariants(ButtonVariant.LUMO_SMALL);

    initUi();
  }

  private void initUi() {
    setAlignItems(Alignment.CENTER);
    add(add, select, remove);
  }

  public void refreshAll() {
    select.getDataProvider().refreshAll();
  }

  public void setPlaceholder(String placeholder) {
    select.setPlaceholder(placeholder);
  }

  public T getValue() {
    return select.getValue();
  }

  public void setValue(T value) {
    select.setValue(value);
  }

  public Select<T> getSelect() {
    return select;
  }

  public interface DynamicSelectListener<T> {

    void onRemove(T t);

    void onSelectionChange(T t);

    void onAdd(T t);
  }
}
