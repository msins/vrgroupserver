package edu.vrgroup.ui.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;

public class ButtonFactory {

  private static Button create(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    return new Button(text, listener);
  }

  public static Button createRedButton(String text,
      ComponentEventListener<ClickEvent<Button>> listener) {
    Button button = create(text, listener);
    button.getElement().getStyle().set("background-color", "var(--lumo-error-color)");
    button.getElement().getStyle().set("color", "var(--lumo-error-contrast-color)");
    button.getElement().getStyle().set("font-weight", "bold");
    return button;
  }

  public static Button createPrimaryButton(String text,
      ComponentEventListener<ClickEvent<Button>> listener) {
    Button button = create(text, listener);
    button.getElement().getStyle().set("background-color", "var(--lumo-primary-color)");
    button.getElement().getStyle().set("color", "var(--lumo-primary-contrast-color)");
    button.getElement().getStyle().set("font-weight", "bold");
    return button;
  }

  public static Button createGreenButton(String text,
      ComponentEventListener<ClickEvent<Button>> listener) {
    Button button = create(text, listener);
    button.getElement().getStyle().set("background-color", "var(--lumo-success-color)");
    button.getElement().getStyle().set("color", "var(--lumo-success-contrast-color)");
    button.getElement().getStyle().set("font-weight", "bold");
    return button;
  }
}
