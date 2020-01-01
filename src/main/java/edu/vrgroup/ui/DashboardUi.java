package edu.vrgroup.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.vrgroup.GameChangeListener;
import edu.vrgroup.ScenarioChangeListener;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.ui.providers.DashboardGridAnswersProvider;
import edu.vrgroup.ui.util.AnswersGrid;
import java.time.format.DateTimeFormatter;

@Route(value = "dashboard", layout = MainAppUi.class)
@PageTitle("Dashboard")
public class DashboardUi extends VerticalLayout implements GameChangeListener, ScenarioChangeListener {

  DashboardGrid grid = new DashboardGrid();
  Game game;
  Scenario scenario;

  public DashboardUi() {
    add(grid);
  }

  @Override
  public void gameChanged(Game game) {
    this.game = game;
    System.out.println("[Dashboard] New game: " + game);
    if (game != null) {
      if (grid.getColumnByKey("game") != null) {
        grid.removeColumn(grid.getColumnByKey("game"));
      }
    }else{
      grid.refreshAllColumns();
    }

    grid.setDataProvider(new DashboardGridAnswersProvider(game, scenario));
    registerToScenarioNotifier(game);
    grid.getDataProvider().refreshAll();
  }

  @Override
  public void scenarioChanged(Scenario scenario) {
    this.scenario = scenario;
    System.out.println("[Dashboard] New scenario: " + scenario);
    if (scenario != null) {
      if (grid.getColumnByKey("scenario") != null) {
        grid.removeColumn(grid.getColumnByKey("scenario"));
      }
    } else {
      grid.refreshAllColumnsButGame();
    }

    grid.setDataProvider(new DashboardGridAnswersProvider(game, scenario));
    grid.getDataProvider().refreshAll();
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    registerToGameNotifier();
  }

  @Override
  protected void onDetach(DetachEvent detachEvent) {
    unregisterFromGameNotifier();
    unregisterFromScenarioNotifier(game);
  }

  private static class DashboardGrid extends AnswersGrid {

    public DashboardGrid() {
      initUi();
    }

    private void initUi() {
      refreshAllColumns();
    }

    public void refreshAllColumnsButGame() {
      refreshAllColumns();
      removeColumnByKey("game");
    }

    public void refreshAllColumns() {
      removeAllColumns();
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
          .setKey("scenario")
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
