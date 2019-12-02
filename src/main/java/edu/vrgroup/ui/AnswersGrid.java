package edu.vrgroup.ui;

import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import edu.vrgroup.model.Answer;

public class AnswersGrid extends Grid<Answer> {

  {
    initStyle();
  }

  private void initStyle() {
    setMultiSort(true);
    getElement().getStyle().set("border-style", "groove");
    getElement().getStyle().set("border-width", "thin");
    getElement().getStyle().set("border-color", "var(--lumo-primary-color-10pct)");
    addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS, GridVariant.LUMO_ROW_STRIPES);
  }

}
