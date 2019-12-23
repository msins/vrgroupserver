package edu.vrgroup.ui.util;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.orderedlayout.FlexLayout;

public class StylizedFlexLayout extends FlexLayout {

  {
    initStyle();
  }

  public StylizedFlexLayout(Component... children) {
    super(children);
  }

  private void initStyle() {
    getElement().getStyle().set("border-style", "groove");
    getElement().getStyle().set("border-width", "thin");
    getElement().getStyle().set("border-color", "var(--lumo-primary-color-10pct)");
    getElement().getStyle().set("border-radius", "var(--lumo-border-radius-m)");
    getElement().getStyle().set("box-shadow", "var(--lumo-boc-shadow-s)");
  }
}
