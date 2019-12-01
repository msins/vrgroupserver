package edu.vrgroup.ui;

import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.AxisType;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.Configuration;
import com.vaadin.flow.component.charts.model.DataProviderSeries;
import com.vaadin.flow.component.charts.model.ListSeries;
import com.vaadin.flow.component.charts.model.Tooltip;
import com.vaadin.flow.component.charts.model.XAxis;
import com.vaadin.flow.component.charts.model.YAxis;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import edu.vrgroup.Answers;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Question;
import java.util.Objects;
import java.util.stream.Stream;

public class ResultChart<T extends Question> extends Chart {

  private ListSeries statistics;
  private T question;

  public ResultChart(T question) {
    super(ChartType.BAR);
    this.question = question;
    initChar();
  }

  private void initChar() {
    Configuration conf = getConfiguration();

    DataProviderSeries<Answer> s = new DataProviderSeries<>(
        new AbstractBackEndDataProvider<Answer, Object>() {
          @Override
          protected Stream<Answer> fetchFromBackEnd(Query<Answer, Object> query) {
            return null;
          }

          @Override
          protected int sizeInBackEnd(Query<Answer, Object> query) {
            return 0;
          }
        }
    );

    //x axis
    XAxis x = new XAxis();
    x.setType(AxisType.CATEGORY);
    x.setCategories(question.getChoices());
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

    conf.setSeries(statistics);
  }
}
