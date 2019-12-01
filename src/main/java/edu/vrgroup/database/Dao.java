package edu.vrgroup.database;

import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.questions.MultipleChoicesQuestion;
import java.util.List;

public interface Dao {

  List<Question> getQuestions(Game game);
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
}
