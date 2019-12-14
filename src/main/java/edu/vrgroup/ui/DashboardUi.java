package edu.vrgroup.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.vrgroup.GameChangeListener;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Game;
import edu.vrgroup.ui.providers.DashboardGridAnswersProvider;
import java.time.format.DateTimeFormatter;

@Route(value = "dashboard", layout = MainAppUi.class)
@PageTitle("Dashboard")
public class DashboardUi extends VerticalLayout implements GameChangeListener {

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    registerToGameNotifier();
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    unregisterFromGameNotifier();
  }

  Grid<Answer> grid = new DashboardGrid();
  Game game;

  public DashboardUi() {
    grid.setDataProvider(new DashboardGridAnswersProvider(game));

    add(grid);
  }

  @Override
  public void gameChanged(Game game) {
    this.game = game;
    if (game != null && grid.getColumnByKey("game") != null) {
      grid.removeColumn(grid.getColumnByKey("game"));
    }
    grid.setDataProvider(new DashboardGridAnswersProvider(game));
    grid.getDataProvider().refreshAll();
  }

  private static class DashboardGrid extends AnswersGrid {

    public DashboardGrid() {
      initUi();
    }

    private void initUi() {
      addColumn(a -> a.getTimestamp().toLocalDateTime()
          .format(DateTimeFormatter.ofPattern("d. MMMM yyyy. HH:mm:ss")))
          .setHeader(new Html("<b>Time</b>"));
      addColumn(a -> a.getUser().getName())
          .setHeader(new Html("<b>User</b>"));
      addColumn(Answer::getIPv4)
          .setHeader(new Html("<b>IPv4</b>"));
      addColumn(Answer::getGame)
          .setKey("game")
          .setHeader(new Html("<b>Game</b>"));
      addColumn(Answer::getScenario)
          .setHeader(new Html("<b>Scenario</b>"));
      addColumn(a -> a.getQuestion().getText())
          .setHeader(new Html("<b>Question</b>"));
      addColumn(Answer::getChoice)
          .setHeader(new Html("<b>Score</b>"));

      setMultiSort(true);
      getColumns().forEach(e -> e.setAutoWidth(true));
      getColumns().forEach(e -> e.setSortable(true));
    }
  }

}
