package edu.vrgroup.ui;

import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.renderer.TextRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import edu.vrgroup.GameChangeListener;
import edu.vrgroup.database.DaoProvider;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import java.util.stream.Stream;

@Route(value = "questions", layout = MainAppUi.class)
@PageTitle("Questions")
public class QuestionsUi extends SplitLayout implements GameChangeListener {

  private Game game;
  ListBox<Question> questions = new ListBox<>();

  @Override
  protected void onAttach(AttachEvent attachEvent) {
    System.out.println("attack to questions " + attachEvent.getSession());
    registerToNotifier();
  }

  public QuestionsUi() {
    if (game != null) {
      questions.setDataProvider(new QuestionsProvider(game));
    }
    setSizeFull();

    questions.setHeightFull();
    questions.setRenderer(new TextRenderer<>(Question::getText));
    addToPrimary(questions);

    questions.addValueChangeListener(e -> {

    });

//    questionsView.addValueChangeListener(e -> {
//      if (curr != null) {
//        remove(curr);
//      }
//      curr = new QuestionView();
//      addToSecondary(curr);
//    });
//
//    questionsView.setItems(questionList);
//
//    Button button = new Button("New question", VaadinIcon.PLUS.create(), e -> {
//      openEditor = new NewQuestionForm();
//      openEditor.open();
//    });
//
//    button.setWidthFull();
//
//    var leftSplit = new VerticalLayout(button, questionsView);
//
//    setSplitterPosition(40);
//    addToPrimary(leftSplit);
//    setHeightFull();
  }

  @Override
  public void gameChanged(Game game) {
    this.game = game;
    questions.setDataProvider(new QuestionsProvider(game));
    questions.getDataProvider().refreshAll();
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

  private static class QuestionView extends VerticalLayout {

    private ResultChart chart;
//    private ResultChart<AbstractScalingQuestion> chart;
//    private Answers<AbstractScalingQuestion> results;
//    private AnswersView updates;
//    private TextArea text;
//    private ButtonGroup operations;
//
//    public QuestionView(Answers<AbstractScalingQuestion> results) {
//      this.results = results;
//      chart = new ResultChart<>(this.results);
//      text = new TextArea("Text", results.getQuestion().getText(), e -> System.out.println(e));
//      updates = new AnswersView("timestamp", "score");
//      var a = new HorizontalLayout(updates, chart);
//      a.setWidthFull();
//
//      //todo add to updates view
//
//      operations = new ButtonGroup();
//      operations.add(ButtonFactory.createGreenButton("Sync", e -> {
//      }));
//      operations.add(ButtonFactory.createRedButton("Delete", e -> {
//      }));
//      operations.setMaxWidth("20%");
//      operations.forEach(HasSize::setWidthFull);
//      text.setWidthFull();
//      HorizontalLayout l = new HorizontalLayout(text, operations);
//      operations.getStyle().set("margin", "auto");
//      operations.getStyle().set("display", "block");
//      l.setWidthFull();
//      add(a, l);
//    }
//
//    public ResultChart<AbstractScalingQuestion> getChart() {
//      return chart;
//    }
//
//    public AnswersView getUpdates() {
//      return updates;
//    }
  }

}
