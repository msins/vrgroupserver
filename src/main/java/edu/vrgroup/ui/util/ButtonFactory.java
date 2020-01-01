package edu.vrgroup.ui.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;

public interface ButtonFactory {

  ButtonVariant RED = ButtonVariant.LUMO_ERROR;
  ButtonVariant GREEN = ButtonVariant.LUMO_SUCCESS;
  ButtonVariant PRIMARY = ButtonVariant.LUMO_PRIMARY;

  default Button createRedButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(create(text, listener), RED);
  }

  default Button createRedButton(Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(create(icon, listener), RED);
  }

  default Button createGreenButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(create(text, listener), GREEN);
  }

  default Button createGreenButton(Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(create(icon, listener), GREEN);
  }

  default Button createPrimaryButton(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(create(text, listener), PRIMARY);
  }

  default Button createPrimaryButton(Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return applyStyle(create(icon, listener), PRIMARY);
  }

  private Button create(Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
    return new Button(icon, listener);
  }

  private Button create(String text, ComponentEventListener<ClickEvent<Button>> listener) {
    return new Button(text, listener);
  }

  default Button applyStyle(Button button, ButtonVariant... variants) {

    //todo if necessary add Map<String, String> to arguments to initialize style here.
    button.getElement().getStyle().set("font-weight", "bold");

    button.addThemeVariants(variants);
    return button;
  }
}
