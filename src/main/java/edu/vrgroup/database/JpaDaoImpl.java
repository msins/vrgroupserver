package edu.vrgroup.database;

import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.GameQuestion;
import edu.vrgroup.model.GameScenario;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.model.User;
import java.sql.Timestamp;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

public class JpaDaoImpl implements Dao {

  @Override
  public List<Question> getQuestions(Game game) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select q from Question as q, GameQuestion as gq where q.id = gq.question.id and gq.game.id = :gameId",
            Question.class)
        .setParameter("gameId", game.getId())
        .getResultList();
  }

  @Override
  public void addQuestion(Game game, Question question) {
    JpaEntityManagerProvider.getEntityManager().persist(question);
    for (var choice : question.getChoices()) {
      JpaEntityManagerProvider.getEntityManager().persist(choice);
    }
    JpaEntityManagerProvider.getEntityManager().persist(new GameQuestion(game, question));
    commit();
  }

  @Override
  public void updateQuestion(Game game, Question question, Question newQuestion) {
    JpaEntityManagerProvider.getEntityManager()
        .createQuery("update Question as q set q.id = :newQuestionId, q.text = :newText")
        .setParameter("newQuestionId", newQuestion.getId())
        .setParameter("newText", newQuestion.getText())
        .executeUpdate();
    commit();
    clearCache();
  }

  @Override
  public void removeQuestion(Question question) {
    EntityManager em = JpaEntityManagerProvider.getEntityManager();
    em.createQuery("delete from Question as q where q.id = :questionId")
        .setParameter("questionId", question.getId()).executeUpdate();
    commit();
    clearCache();
  }

  @Override
  public int getQuestionsCount(Game game) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select count(*) from Question as q, GameQuestion as gq where q.id = gq.question.id and gq.game.id = :gameId",
            Long.class)
        .setParameter("gameId", game.getId())
        .getSingleResult().intValue();
  }

  @Override
  public List<Choice> getChoices(Question question) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select choice from Choice as choice where choice.question.id = :questionId", Choice.class)
        .setParameter("questionId", question.getId())
        .setMaxResults(50)
        .getResultList();
  }

  @Override
  public int getGamesCount() {
    return ((Long) JpaEntityManagerProvider.getEntityManager()
        .createQuery("select count(*) from Game")
        .getSingleResult()).intValue();
  }

  @Override
  public List<Game> getGames() {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select game from Game as game", Game.class)
        .setMaxResults(50)
        .getResultList();
  }

  @Override
  public Game getGame(String gameName) {
    try {
      return JpaEntityManagerProvider.getEntityManager()
          .createQuery("select game from Game as game where game.name = :gameName", Game.class)
          .setParameter("gameName", gameName).getSingleResult();
    } catch (NoResultException e) {
      return null;
    }
  }

  @Override
  public void addGame(Game game) {
    JpaEntityManagerProvider.getEntityManager().persist(game);
    JpaEntityManagerProvider.getEntityManager().persist(new GameScenario(game, Scenario.DEFAULT));
    commit();
  }

  @Override
  public void removeGame(Game game) {
    EntityManager em = JpaEntityManagerProvider.getEntityManager();
    em.remove(em.contains(game) ? game : em.merge(game));
    commit();
  }

  @Override
  public int getAnswersCount() {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select count(*) from Answer", Long.class)
        .getSingleResult().intValue();
  }

  @Override
  public List<Answer> getAnswers(int offset, int limit) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select answer from Answer as answer", Answer.class)
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultList();
  }

  @Override
  public int getAnswersCount(Game game) {
    if (game == null) {
      return getAnswersCount();
    }
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select count(*) from Answer as answer where answer.game.id = :gameId", Long.class)
        .setParameter("gameId", game.getId())
        .getSingleResult().intValue();
  }

  @Override
  public List<Answer> getAnswers(Game game, int offset, int limit) {
    if (game == null) {
      return getAnswers(offset, limit);
    }

    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select answer from Answer as answer where answer.game.id = :gameId", Answer.class)
        .setParameter("gameId", game.getId())
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultList();
  }

  @Override
  public List<Answer> getAnswers(Game game, Scenario scenario, Question question, int offset, int limit) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select answer from Answer as answer where answer.game.id = :gameId and answer.scenario.id = :scenarioId and answer.question.id = :questionId",
            Answer.class)
        .setParameter("gameId", game.getId())
        .setParameter("scenarioId", scenario.getId())
        .setParameter("questionId", question.getId())
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultList();
  }

  @Override
  public List<Answer> getAllAnswers(Game game, Scenario scenario, Question question) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select answer from Answer as answer where answer.game.id = :gameId and answer.scenario.id = :scenarioId and answer.question.id = :questionId",
            Answer.class)
        .setParameter("gameId", game.getId())
        .setParameter("scenarioId", scenario.getId())
        .setParameter("questionId", question.getId())
        .getResultList();
  }

  @Override
  public int getAnswersCount(Game game, Scenario scenario, Question question) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select count(*) from Answer as answer where answer.game.id = :gameId and answer.scenario.id = :scenarioId and answer.question.id = :questionId",
            Long.class)
        .setParameter("gameId", game.getId())
        .setParameter("scenarioId", scenario.getId())
        .setParameter("questionId", question.getId())
        .getSingleResult().intValue();
  }

  @Override
  public void addAnswer(Game game, Scenario scenario, Question question, Choice choice, User user, Timestamp timestamp,
      String IPv4) {
    Answer<Question> answer = new Answer<>(timestamp, question, scenario, game, user, choice, IPv4);
    JpaEntityManagerProvider.getEntityManager().persist(answer);
    commit();
  }

  @Override
  public void addUser(User user) {
    JpaEntityManagerProvider.getEntityManager().persist(user);
    commit();
  }

  @Override
  public void addScenario(Scenario scenario) {
    JpaEntityManagerProvider.getEntityManager().persist(scenario);
    commit();
  }

  @Override
  public Scenario getDefaultScenario() {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select s from Scenario as s where s.name='Default'", Scenario.class)
        .getSingleResult();
  }

  private void clearCache() {
    JpaEntityManagerProvider.getEntityManager().clear();
  }

  private void commit() {
    JpaEntityManagerProvider.close();
  }
}
