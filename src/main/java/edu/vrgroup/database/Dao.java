package edu.vrgroup.database;

import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.questions.MultipleChoicesQuestion;
import java.util.List;
import java.util.Map;

public interface Dao {

  List<Question> getQuestions(Game game);

  void addQuestion(Game game, Question question);

  int getQuestionsCount(Game game);

  List<Choice> getChoices(MultipleChoicesQuestion question);

  int getGamesCount();

  List<Game> getGames();

  void addGame(Game game);

  void removeGame(Game game);

  int getAnswersCount();

  List<Answer> getAnswers(int offset, int limit);

  int getAnswersCount(Game game);

  List<Answer> getAnswers(Game game, int offset, int limit);

  List<Answer> getAnswers(Game game, Scenario scenario, Question question, int offset, int limit);

  int getAnswersCount(Game game, Scenario scenario, Question question);

  Map<Choice, Integer> getQuestionStatistics(Game game, Scenario scenario, Question question);
}
