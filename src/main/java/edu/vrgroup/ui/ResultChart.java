package edu.vrgroup.ui;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Question;

public class ResultChart<T extends Question> extends Chart {

  private T question;

  public ResultChart(T question) {
    super(ChartType.BAR);
    this.question = question;
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
    setEmptySeries();
  }

  public void update(ListSeries series) {
    getConfiguration().setSeries(series);
  }

  public T getQuestion() {
    return question;
  }

  private void setEmptySeries() {
    Number[] nums = new Number[question.getChoices().size()];
    getConfiguration().setSeries(new ListSeries(nums));
  }
}
