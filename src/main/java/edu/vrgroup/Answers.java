package edu.vrgroup;

import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Game;
import edu.vrgroup.model.Question;
import edu.vrgroup.model.Scenario;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Answers<T extends Question> {

  private int count;
  private Map<Choice, Integer> counter = new HashMap<>();
  private Game game;
  private Scenario scenario;
  private T question;

  public Answers(T question) {
    this.question = question;
  }

  public List<Number> getStatistics() {
    return counter.entrySet().stream().sorted().map(e -> (e.getValue() / (double) count) * 100)
        .collect(Collectors.toList());
  }

}
