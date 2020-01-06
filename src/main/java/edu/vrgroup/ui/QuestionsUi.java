package edu.vrgroup.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.data.renderer.TemplateRenderer;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.vrgroup.GameChangeListener;
import edu.vrgroup.ScenarioChangeListener;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.ui.forms.NewQuestionForm;
import edu.vrgroup.ui.providers.AnswersGridDataProvider;
import edu.vrgroup.ui.providers.QuestionsProvider;
import edu.vrgroup.ui.util.AbstractButtonFactory;
import edu.vrgroup.ui.util.AnswersGrid;
import edu.vrgroup.ui.util.ResultChart;
import edu.vrgroup.ui.util.StylizedHorizontalLayout;
import edu.vrgroup.ui.util.StylizedList;
import edu.vrgroup.ui.util.StylizedVerticalLayout;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Route(value = "questions", layout = MainAppUi.class)
@PageTitle("Questions")
public class QuestionsUi extends StylizedHorizontalLayout implements GameChangeListener, ScenarioChangeListener {

  private Game game;
  private Scenario scenario;
  private QuestionList questionList;
  private QuestionInformation questionInformation;

  public QuestionsUi() {
    questionList = new QuestionList();

    if (game != null && scenario != null) {
      questionList.setDataProvider(new QuestionsProvider(scenario));
    }
    setSizeFull();

    questionList.addValueChangeListener(list -> {
      QuestionInformation old = questionInformation;
      if (list.getValue() != null) {
        questionInformation = new QuestionInformation(
            this.game,
            this.scenario,
            list.getValue(),
            question -> {
              DaoProvider.getDao().removeQuestion(question);
              questionList.getDataProvider().refreshAll();
            },
            (question, newText) -> {
              DaoProvider.getDao().updateQuestion(question, newText);
              questionList.getDataProvider().refreshAll();
              questionList.setValue(question);
            });
      }

      replace(old, questionInformation);
      add(questionInformation);
    });

    Button newQuestionBtn = new Button("New question", VaadinIcon.PLUS.create(),
        e -> new NewQuestionForm(scenario, question -> questionList.getDataProvider().refreshAll()).open());
    newQuestionBtn.setWidthFull();

    StylizedVerticalLayout listLayout = new StylizedVerticalLayout(newQuestionBtn, questionList);
    listLayout.setAlignItems(Alignment.CENTER);
    listLayout.setMaxWidth("25%");
    listLayout.setMinWidth("25%");
    add(listLayout);
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

  @Override
  public void gameChanged(Game game) {
    this.game = game;
    registerToScenarioNotifier(game);
    questionList.getDataProvider().refreshAll();
    if (questionInformation != null) {
      questionInformation.removeAll();
    }
  }

  @Override
  public void scenarioChanged(Scenario scenario) {
    this.scenario = scenario;
    if (questionInformation != null) {
      questionInformation.removeAll();
    }
    if (scenario == null) {
      return;
    }

    questionList.setDataProvider(new QuestionsProvider(scenario));
  }

  private static class QuestionList extends StylizedList<Question> {

    {
      setHeightFull();
      setWidthFull();
      setRenderer(new TextRenderer<>(Question::getText));
    }
  }

  private static class QuestionInformation extends StylizedVerticalLayout {

    private ResultChart<Question> chart;
    private Grid<Answer> grid;
    private Game game;
    private Scenario scenario;
    private Question question;

    public QuestionInformation(Game game, Scenario scenario, Question question, Consumer<Question> onDelete,
        BiConsumer<Question, String> onSync) {
      this.game = game;
      this.scenario = scenario;
      this.question = question;
      initUi(onDelete, onSync);
    }

    private void initUi(Consumer<Question> onDelete, BiConsumer<Question, String> onSync) {
      chart = new ResultChart<>(question);
      chart.setWidthFull();

      grid = new QuestionAnswerGrid(game, scenario, question);
      grid.setWidthFull();

      VerticalLayout layout = new VerticalLayout();
      layout.setAlignItems(Alignment.CENTER);

      HorizontalLayout gridChart = new HorizontalLayout(grid, chart);
      gridChart.setWidthFull();
      layout.add(gridChart);

      TextArea text = new TextArea("Text");
      text.setValue(question.getText());
      text.setWidthFull();
      layout.add(text);

      Button refresh = AbstractButtonFactory.getCircular().createPrimaryButton(VaadinIcon.REFRESH.create(), e -> {
        //todo fix
        List<Answer> answers = DaoProvider.getDao().getAnswers(game, scenario, question, 0, Integer.MAX_VALUE).collect(
            Collectors.toList());
        Number[] stats = QuestionStatistics.get(answers, question);
        chart.refresh(stats);
        grid.getDataProvider().refreshAll();
      });
      refresh.click();

      Button delete = AbstractButtonFactory.getCircular().createRedButton(VaadinIcon.CLOSE.create(), e -> {
        onDelete.accept(question);
        removeAll();
      });

      Button sync = AbstractButtonFactory.getCircular()
          .createGreenButton(VaadinIcon.ARCHIVE.create(), e -> {
            onSync.accept(question, text.getValue());
            e.getSource().setEnabled(false);
          });
      sync.setEnabled(false);
      text.addKeyPressListener(e -> sync.setEnabled(true));

      layout.add(new StylizedHorizontalLayout(refresh, delete, sync));
      add(layout);
    }
  }

  private static final class QuestionStatistics {

    public static Number[] get(List<Answer> answers, Question question) {
      Map<Choice, Integer> counter = new TreeMap<>();
      question.getChoices().forEach(c -> counter.put(c, 0));
      answers.forEach(a -> counter.merge(a.getChoice(), 1, Integer::sum));
      return counter.values().stream()
          .map(value -> (value.doubleValue() / answers.size()) * 100)
          .toArray(Number[]::new);
    }
  }

  private static class QuestionAnswerGrid extends AnswersGrid {

    private Game game;
    private Scenario scenario;
    private Question question;

    public QuestionAnswerGrid(Game game, Scenario scenario, Question question) {
      this.game = game;
      this.scenario = scenario;
      this.question = question;
      initUi();
    }

    private void initUi() {
      addColumn(a -> a.getTimestamp().toLocalDateTime()
          .format(DateTimeFormatter.ofPattern("d.MM.yyyy. HH:mm:ss")))
          .setHeader(new Html("<b>Time</b>"));
      addColumn(
          TemplateRenderer.<Answer>of("<div>[[item.user.name]]<b>[</b>[[item.IPv4]]<b>]</b></div>")
              .withProperty("user", Answer::getUser)
              .withProperty("IPv4", Answer::getIPv4))
          .setHeader(new Html("<b>User</b>"));
      addColumn(Answer::getChoice)
          .setHeader(new Html("<b>Score</b>"));

      getColumns().forEach(e -> e.setAutoWidth(true));
      getColumns().forEach(e -> e.setSortable(true));
      setDataProvider(new AnswersGridDataProvider(game, scenario, question));
    }
  }
}
