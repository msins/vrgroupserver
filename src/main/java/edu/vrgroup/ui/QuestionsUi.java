package edu.vrgroup.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.DataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import edu.vrgroup.GameChangeListener;
import edu.vrgroup.GameChangeNotifier;
import edu.vrgroup.ScenarioChangeListener;
import edu.vrgroup.ScenarioChangeNotifier;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.questions.NewQuestionForm;
import edu.vrgroup.ui.util.ButtonFactory;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

@Route(value = "questions", layout = MainAppUi.class)
@PageTitle("Questions")
public class QuestionsUi extends HorizontalLayout implements GameChangeListener, ScenarioChangeListener {

  private Game game;
  private Questions questions;
  private QuestionView questionInformation;
  private ScenarioChangeNotifier scenarioNotifier = new ScenarioChangeNotifier();

  public QuestionsUi() {
    scenarioNotifier.setScenario(Scenario.DEFAULT);
    questions = new Questions();

    if (game != null) {
      questions.setDataProvider(new QuestionsProvider(game));
    }
    setSizeFull();

    questions.addValueChangeListener(e -> {
      QuestionView old = questionInformation;
      if (e.getValue() != null) {
        questionInformation = new QuestionView(game,
            scenarioNotifier.getScenario(),
            e.getValue(),
            questions.getDataProvider());
      }

      replace(old, questionInformation);
      add(questionInformation);
    });

    Button button = new Button("New question", VaadinIcon.PLUS.create(),
        e -> new NewQuestionForm(game, question -> questions.getDataProvider().refreshAll()).open()) {{
      setWidthFull();
    }};
    add(new VerticalLayout(button, questions) {{
      setMaxWidth("30%");
      setMinWidth("30%");
    }});
  }

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    System.out.println("attach to questions " + attachEvent.getSession());

    registerToGameNotifier();
    Game game = ((GameChangeNotifier) VaadinSession.getCurrent().getAttribute("game.notifier")).getGame();
    if (game != null) {
      gameChanged(game);
    }
  }

  @Override
  public void gameChanged(Game game) {
    this.game = game;
    questions.setDataProvider(new QuestionsProvider(game));
    questions.getDataProvider().refreshAll();
    if (questionInformation != null) {
      questionInformation.removeAll();
    }
  }

  @Override
  public void scenarioChanged(Scenario scenario) {
    questionInformation.setScenario(scenario);
  }

  @Override
  public void registerToScenarioNotifier() {
    scenarioNotifier.registerListener(this);
  }

  private static class QuestionsProvider extends AbstractBackEndDataProvider<Question, Object> {

    private Game game;

    public QuestionsProvider(Game game) {
      this.game = game;
    }

    @Override
    protected Stream<Question> fetchFromBackEnd(Query<Question, Object> query) {
      return DaoProvider.getDao().getQuestions(game).stream();
    }

    @Override
    protected int sizeInBackEnd(Query<Question, Object> query) {
      return DaoProvider.getDao().getQuestionsCount(game);
    }
  }

  private static class Questions extends ListBox<Question> {

    {
      getElement().getStyle().set("border-style", "groove");
      getElement().getStyle().set("border-width", "thin");
      getElement().getStyle().set("border-color", "var(--lumo-primary-color-10pct)");
      getElement().getStyle().set("border-radius", "var(--lumo-border-radius-m)");
      getElement().getStyle().set("box-shadow", "var(--lumo-boc-shadow-s)");
      setHeightFull();
      setRenderer(new TextRenderer<>(Question::getText));
      setWidthFull();
    }

    public Questions() {
    }
  }

  private static class QuestionView extends VerticalLayout {

    private ResultChart<Question> chart;
    private AnswersGrid grid;
    private Game game;
    private Scenario scenario;
    private Question question;
    private DataProvider<Question, ?> notifier;

    public QuestionView(Game game, Scenario scenario, Question question, DataProvider<Question, ?> notifier) {
      this.game = game;
      this.scenario = scenario;
      this.question = question;
      this.notifier = notifier;
      initUi();
    }

    public void setGame(Game game) {
      this.game = game;
    }

    public void setScenario(Scenario scenario) {
      this.scenario = scenario;
    }

    public void setQuestion(Question question) {
      this.question = question;
    }

    private void initUi() {
      chart = new ResultChart<>(question);
      grid = new AnswersGrid();
      chart.setWidthFull();
      grid.setWidthFull();
      grid.addColumn(a -> a.getTimestamp().toLocalDateTime()
          .format(DateTimeFormatter.ofPattern("d.MM.yyyy. HH:mm:ss")))
          .setHeader(new Html("<b>Time</b>"));
      grid.addColumn(
          TemplateRenderer.<Answer>of("<div>[[item.user.name]]<b>[</b>[[item.IPv4]]<b>]</b></div>")
              .withProperty("user", Answer::getUser)
              .withProperty("IPv4", Answer::getIPv4))
          .setHeader(new Html("<b>User</b>"));
      grid.addColumn(Answer::getChoice)
          .setHeader(new Html("<b>Score</b>"));

      grid.getColumns().forEach(e -> e.setAutoWidth(true));
      grid.getColumns().forEach(e -> e.setSortable(true));
      grid.setDataProvider(new AbstractBackEndDataProvider<>() {
        @Override
        protected Stream<Answer> fetchFromBackEnd(Query<Answer, Object> query) {
          return DaoProvider.getDao().getAnswers(game, scenario, question, query.getOffset(), query.getLimit())
              .stream();
        }

        @Override
        protected int sizeInBackEnd(Query<Answer, Object> query) {
          return DaoProvider.getDao().getAnswersCount(game, scenario, question);
        }
      });
      add(new Button("xd", e -> {
        System.err.println(DaoProvider.getDao().getQuestionStatistics(game, scenario, question));

      }));
      VerticalLayout layout = new VerticalLayout();
      layout.setAlignItems(Alignment.CENTER);
      HorizontalLayout gridChart = new HorizontalLayout(grid, chart);
      gridChart.setWidthFull();
      layout.add(gridChart);

      TextArea text = new TextArea("Text");
      text.setValue(question.getText());
      text.setWidthFull();
      layout.add(text);

      Button delete = ButtonFactory.createRedButton("Delete", e -> {
        DaoProvider.getDao().removeQuestion(question);
        removeAll();
        notifier.refreshAll();
      });
      Button sync = ButtonFactory.createGreenButton("Sync", e -> DaoProvider.getDao().updateQuestion(game, question, new Question(text.getValue())));
      sync.setEnabled(false);
      text.addKeyPressListener(e -> sync.setEnabled(true));

      layout.add(new HorizontalLayout(delete, sync));
      add(layout);
    }
  }

}
