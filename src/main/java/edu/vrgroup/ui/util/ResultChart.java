package edu.vrgroup.ui.util;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import edu.vrgroup.database.Dao;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

public class ResultChart<T extends Question> extends Chart {

  private T question;
  private ListSeries series = new ListSeries();

  public ResultChart(T question) {
    super(ChartType.BAR);
    this.question = Objects.requireNonNull(question);
    initChar();
  }

  private void initChar() {
    Configuration conf = getConfiguration();

    //x axis
    XAxis x = new XAxis();
    x.setType(AxisType.CATEGORY);
    x.setCategories(question.getChoices().stream().sorted().map(Choice::getValue).toArray(String[]::new));
    x.setTickLength(0);
    conf.addxAxis(x);

    //y axis
    YAxis y = new YAxis();
    y.getLabels().setEnabled(false);
    y.setTickAmount(0);
    y.setMin(0);
    y.setMax(100);
    y.setTitle("");
    conf.addyAxis(y);

    //detail info on hover
    Tooltip tooltip = new Tooltip();
    tooltip.setShared(true);
    tooltip.setAnimation(true);
    conf.getLegend().setEnabled(false);
    tooltip.setFormatter("function() { " +
        "return this.y.toFixed(1) + '%';" +
        "}");
    conf.setTooltip(tooltip);
    series.setData(getEmptySeries());
    getConfiguration().addSeries(series);
  }

  public void refresh(Number[] stats) {
    this.series.setData(stats);
    series.updateSeries();
  }

  public T getQuestion() {
    return question;
  }

  private Number[] getEmptySeries() {
    return new Number[question.getChoices().size()];
  }
}
