package edu.vrgroup.ui.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;

public class ButtonFactory {

  private static Button create(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    return new Button(text, listener);
  }

  public static Button createRedButton(String text,
      ComponentEventListener<ClickEvent<Button>> listener) {
    Button button = create(text, listener);
    button.addThemeVariants(ButtonVariant.LUMO_ERROR);
    button.getElement().getStyle().set("font-weight", "bold");
    return button;
  }

  public static Button createPrimaryButton(String text,
      ComponentEventListener<ClickEvent<Button>> listener) {
    Button button = create(text, listener);
    button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    button.getElement().getStyle().set("font-weight", "bold");
    return button;
  }

  public static Button createGreenButton(String text,
      ComponentEventListener<ClickEvent<Button>> listener) {
    Button button = create(text, listener);
    button.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
    button.getElement().getStyle().set("font-weight", "bold");
    return button;
  }
}
