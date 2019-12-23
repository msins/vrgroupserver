package edu.vrgroup.ui.util;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;

public final class AbstractButtonFactory {

  private enum ButtonType implements ButtonFactory {
    CIRCULAR {
      @Override
      public Button create(Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
        return new Button(icon, listener);
      }

      @Override
      public Button create(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        return new Button(text, listener);
      }

      @Override
      public Button applyStyle(Button button, ButtonVariant... variants) {
        super.applyStyle(button, variants);
        button.getStyle().set("border-radius", "50%");
        button.getStyle().set("padding", "0px");
        return button;
      }
    },
    RECTANGLE {
      @Override
      public Button create(Icon icon, ComponentEventListener<ClickEvent<Button>> listener) {
        return new Button(icon, listener);
      }

      @Override
      public Button create(String text, ComponentEventListener<ClickEvent<Button>> listener) {
        return new Button(text, listener);
      }
    }
  }

  public static ButtonFactory getCircular() {
    return ButtonType.CIRCULAR;
  }

  public static ButtonFactory getRectangle() {
    return ButtonType.RECTANGLE;
  }
}
