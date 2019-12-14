package edu.vrgroup.database;

import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.model.User;
import java.sql.Timestamp;
import java.util.List;

public interface Dao {

  List<Question> getQuestions(Game game);

  void addQuestion(Game game, Question question);

  void removeQuestion(Question question);

  void updateQuestion(Question question, String newQuestion);

  int getQuestionsCount(Game game);

  List<Choice> getChoices(Question question);

  int getGamesCount();

  List<Game> getGames();

  Game getGame(String gameName);

  void addGame(Game game);

  void removeGame(Game game);

  int getAnswersCount();

  List<Answer> getAnswers(int offset, int limit);

  int getAnswersCount(Game game);

  List<Answer> getAnswers(Game game, int offset, int limit);

  List<Answer> getAnswers(Game game, Scenario scenario, Question question, int offset, int limit);

  List<Answer> getAllAnswers(Game game, Scenario scenario, Question question);

  void addAnswer(Game game, Scenario scenario, Question question, Choice choice, User user, Timestamp timestamp,
      String IPv4);

  int getAnswersCount(Game game, Scenario scenario, Question question);

  void addUser(User user);

  void addScenario(Scenario scenario);

  Scenario getDefaultScenario();
}
