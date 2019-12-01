package edu.vrgroup.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.vrgroup.GameChangeListener;
import edu.vrgroup.GameChangeNotifier;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Game;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Route(value = "dashboard", layout = MainAppUi.class)
@PageTitle("Dashboard")
public class DashboardUi extends VerticalLayout implements GameChangeListener {

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    registerToNotifier();
  }

  AnswersView grid = new AnswersView();
  Game game;

  public DashboardUi() {
    grid.setMultiSort(true);
    grid.setDataProvider(new AnswersProvider(game));

    grid.addColumn(
        a -> a.getTimestamp().toLocalDateTime()
            .format(DateTimeFormatter.ofPattern("d. MMMM yyyy. HH:mm:ss")))
        .setFlexGrow(1)
        .setHeader(new Html("<b>Timestamp</b>"))
        .setSortable(true);
    grid.addColumn(a -> a.getUser().getName())
        .setSortable(true)
        .setHeader(new Html("<b>Name</b>"));
    grid.addColumn(Answer::getIPv4)
        .setSortable(true)
        .setHeader(new Html("<b>IPv4</b>"));
    grid.addColumn(Answer::getGame)
        .setKey("game")
        .setSortable(true)
        .setHeader(new Html("<b>Game</b>"));
    grid.addColumn(Answer::getScenario)
        .setSortable(true)
        .setHeader(new Html("<b>Scenario</b>"));
    grid.addColumn(a -> a.getQuestion().getText())
        .setSortable(true)
        .setHeader(new Html("<b>Question</b>"));
    grid.addColumn(a -> a.getQuestion().getChoices()[a.getScore() - 1])
        .setSortable(true)
        .setHeader(new Html("<b>Score</b>"));

    add(grid);
  }

  @Override
  public void gameChanged(Game game) {
    this.game = game;
    if (game != null && grid.getColumnByKey("game") != null) {
      grid.removeColumn(grid.getColumnByKey("game"));
    }
    grid.setDataProvider(new AnswersProvider(game));
    grid.getDataProvider().refreshAll();
  }

  static class AnswersProvider extends AbstractBackEndDataProvider<Answer, Object> {

    private Game game;

    public AnswersProvider(Game game) {
      this.game = game;
    }

    @Override
    protected Stream<Answer> fetchFromBackEnd(Query<Answer, Object> query) {
      System.out.println("offset: " + query.getOffset() + ", limit: " + query.getLimit());
      return DaoProvider.getDao().getAnswers(game, query.getOffset(), query.getLimit()).stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Answer, Object> query) {
      return DaoProvider.getDao().getAnswersCount(game);
    }
  }
}
