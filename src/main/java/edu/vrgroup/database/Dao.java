package edu.vrgroup.database;

import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.model.User;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;

public interface Dao {

  //QUESTION DAO API

  Stream<Question> getAllQuestions();

  int getQuestionCount();

  Stream<Question> getQuestions(Scenario scenario);

  void addQuestion(Scenario scenario, Question question);

  void removeQuestion(Question question);

  void updateQuestion(Question question, String newQuestion);

  int getQuestionsCount(Scenario scenario);

  //CHOICE DAO API

  List<Choice> getChoices(Question question);

  //GAME DAO API

  int getGamesCount();

  Stream<Game> getGames();

  Game getGame(String gameName);

  void addGame(Game game);

  void removeGame(Game game);

  //ANSWERS DAO API

  int getAnswersCount();

  int getAnswersCount(Game game);

  int getAnswersCount(Game game, Scenario scenario);

  int getAnswersCount(Game game, Scenario scenario, Question question);

  Stream<Answer> getAnswers(int offset, int limit);

  Stream<Answer> getAnswers(Game game, int offset, int limit);

  Stream<Answer> getAnswers(Game game, Scenario scenario, int offset, int limit);

  Stream<Answer> getAnswers(Game game, Scenario scenario, Question question, int offset, int limit);

  void addAnswer(Game game, Scenario scenario, Question question, Choice choice, User user, Timestamp timestamp,
      String IPv4);

  //USER DAO API

  void addUser(User user);

  //SCENARIO DAO API

  Stream<Scenario> getScenarios(Game game);

  int getScenarioCount(Game game);

  void addScenario(Game game, Scenario scenario);

  void removeScenario(Scenario scenario);
}