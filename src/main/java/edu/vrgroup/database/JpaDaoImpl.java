package edu.vrgroup.database;

import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.GameScenario;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import edu.vrgroup.model.ScenarioQuestion;
import edu.vrgroup.model.User;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.validation.constraints.Null;

public class JpaDaoImpl implements Dao {

  //QUESTIONS DAO API

  @Override
  public Stream<Question> getAllQuestions() {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select q from Question as q", Question.class)
        .getResultStream();
  }

  @Override
  public int getQuestionCount() {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select count(*) from Question", Long.class)
        .getSingleResult().intValue();
  }

  @Override
  public Stream<Question> getQuestions(@Nonnull Scenario scenario) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select q from Question as q, ScenarioQuestion as sq where q.id = sq.question.id and sq.scenario.id = :scenarioId",
            Question.class)
        .setParameter("scenarioId", scenario.getId())
        .getResultStream();
  }

  @Override
  public void addQuestion(@Nonnull Scenario scenario, @Nonnull Question question) {
    JpaEntityManagerProvider.getEntityManager().persist(question);
    for (var choice : question.getChoices()) {
      JpaEntityManagerProvider.getEntityManager().persist(choice);
    }
    JpaEntityManagerProvider.getEntityManager().persist(new ScenarioQuestion(scenario, question));
    commit();
  }

  @Override
  public void updateQuestion(@Nonnull Question question, @Nonnull String newText) {
    JpaEntityManagerProvider.getEntityManager()
        .createQuery("update Question as q set q.text = :newText where q.id = :questionId")
        .setParameter("newText", newText)
        .setParameter("questionId", question.getId())
        .executeUpdate();
    commit();
    clearCache();
  }

  @Override
  public void removeQuestion(@Nonnull Question question) {
    EntityManager em = JpaEntityManagerProvider.getEntityManager();
    em.createQuery("delete from Question as q where q.id = :questionId")
        .setParameter("questionId", question.getId()).executeUpdate();
    commit();
    clearCache();
  }

  @Override
  public int getQuestionsCount(@Nonnull Scenario scenario) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select count(*) from Question as q, ScenarioQuestion as sq where q.id = sq.question.id and sq.scenario.id = :scenarioId",
            Long.class)
        .setParameter("scenarioId", scenario.getId())
        .getSingleResult().intValue();
  }

  //CHOICES DAO API

  @Override
  public List<Choice> getChoices(@Nonnull Question question) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select choice from Choice as choice where choice.question.id = :questionId", Choice.class)
        .setParameter("questionId", question.getId())
        .getResultList();
  }

  //GAME DAO API

  @Override
  public int getGamesCount() {
    return ((Long) JpaEntityManagerProvider.getEntityManager()
        .createQuery("select count(*) from Game")
        .getSingleResult()).intValue();
  }

  @Override
  public Stream<Game> getGames() {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select game from Game as game", Game.class)
        .getResultStream();
  }

  @Override
  @Nullable
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
  public void addGame(@Nonnull Game game) {
    JpaEntityManagerProvider.getEntityManager().persist(game);
    commit();
  }

  @Override
  public void removeGame(@Nonnull Game game) {
    EntityManager em = JpaEntityManagerProvider.getEntityManager();
    em.remove(em.contains(game) ? game : em.merge(game));
    commit();
  }

  //ANSWERS DAO API

  @Override
  public Stream<Answer> getAnswers(int offset, int limit) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select answer from Answer as answer", Answer.class)
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultStream();
  }

  @Override
  public int getAnswersCount() {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select count(*) from Answer", Long.class)
        .getSingleResult().intValue();
  }

  @Override
  public int getAnswersCount(@Nonnull Game game) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select count(*) from Answer as answer where answer.game.id = :gameId", Long.class)
        .setParameter("gameId", game.getId())
        .getSingleResult().intValue();
  }

  @Override
  public int getAnswersCount(@Nonnull Game game, @Nonnull Scenario scenario) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select count(*) from Answer as answer where answer.game.id = :gameId and answer.scenario.id = :scenarioId",
            Long.class)
        .setParameter("gameId", game.getId())
        .setParameter("scenarioId", scenario.getId())
        .getSingleResult().intValue();
  }

  @Override
  public int getAnswersCount(@Nonnull Game game, @Nonnull Scenario scenario, @Nonnull Question question) {
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
  public Stream<Answer> getAnswers(@Nonnull Game game, int offset, int limit) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery("select answer from Answer as answer where answer.game.id = :gameId", Answer.class)
        .setParameter("gameId", game.getId())
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultStream();
  }

  @Override
  public Stream<Answer> getAnswers(@Nonnull Game game, @Nonnull Scenario scenario, int offset, int limit) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select answer from Answer as answer where answer.game.id = :gameId and answer.scenario.id=:scenarioId",
            Answer.class)
        .setParameter("gameId", game.getId())
        .setParameter("scenarioId", scenario.getId())
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultStream();
  }

  @Override
  public Stream<Answer> getAnswers(@Nonnull Game game, @Nonnull Scenario scenario, @Nonnull Question question,
      int offset,
      int limit) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select answer from Answer as answer where answer.game.id = :gameId and answer.scenario.id = :scenarioId and answer.question.id = :questionId",
            Answer.class)
        .setParameter("gameId", game.getId())
        .setParameter("scenarioId", scenario.getId())
        .setParameter("questionId", question.getId())
        .setFirstResult(offset)
        .setMaxResults(limit)
        .getResultStream();
  }

  @Override
  public void addAnswer(Game game, Scenario scenario, Question question, Choice choice, User user, Timestamp timestamp,
      String IPv4) {
    Answer answer = new Answer(timestamp, question, scenario, game, user, choice, IPv4);
    JpaEntityManagerProvider.getEntityManager().persist(answer);
    commit();
  }

  //USER DAO API

  @Override
  public void addUser(@Nonnull User user) {
    JpaEntityManagerProvider.getEntityManager().persist(user);
    commit();
  }

  //SCENARIO DAO API

  @Override
  public void addScenario(@Nonnull Game game, @Nonnull Scenario scenario) {
    JpaEntityManagerProvider.getEntityManager().persist(scenario);
    JpaEntityManagerProvider.getEntityManager().persist(new GameScenario(game, scenario));
    commit();
  }

  @Override
  public void removeScenario(@Nonnull Scenario scenario) {
    EntityManager em = JpaEntityManagerProvider.getEntityManager();
    em.remove(em.contains(scenario) ? scenario : em.merge(scenario));
    commit();
    clearCache();
  }

  @Override
  public int getScenarioCount(@Nonnull Game game) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select count(*) from Scenario as scenario, GameScenario as gs where scenario.id = gs.scenario.id and gs.game.id = :gameId",
            Long.class)
        .setParameter("gameId", game.getId())
        .getSingleResult().intValue();
  }

  @Override
  public Stream<Scenario> getScenarios(@Nonnull Game game) {
    return JpaEntityManagerProvider.getEntityManager()
        .createQuery(
            "select scenario from Scenario as scenario, GameScenario as gs where scenario.id = gs.scenario.id and gs.game.id=:gameId",
            Scenario.class)
        .setParameter("gameId", game.getId())
        .getResultStream();
  }

  //HELPER METHODS

  private void clearCache() {
    JpaEntityManagerProvider.getEntityManager().clear();
  }

  private void commit() {
    JpaEntityManagerProvider.close();
  }
}