package edu.vrgroup.ui.util;

import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class StylizedHorizontalLayout extends HorizontalLayout {

  {
    initStyle();
  }

  private void initStyle() {
    getElement().getStyle().set("border-style", "groove");
    getElement().getStyle().set("border-width", "thin");
    getElement().getStyle().set("border-color", "var(--lumo-primary-color-10pct)");
    getElement().getStyle().set("border-radius", "var(--lumo-border-radius-m)");
    getElement().getStyle().set("box-shadow", "var(--lumo-boc-shadow-s)");
  }

}
