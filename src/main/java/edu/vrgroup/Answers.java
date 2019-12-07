package edu.vrgroup;

import edu.vrgroup.model.Answer;
import edu.vrgroup.model.Choice;
import edu.vrgroup.model.Question;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Answers<T extends Question> {
  private int count;
  private Map<Choice, Integer> counter = new HashMap<>();
  private Deque<Answer> answers = new ArrayDeque<>();
  private T question;

  public Answers(T question) {
    this.question = question;
  }

  public List<Number> getStatistics() {
    return List.of(
        (counter.getOrDefault(5, 0) / (double) count) * 100,
        (counter.getOrDefault(4, 0) / (double) count) * 100,
        (counter.getOrDefault(3, 0) / (double) count) * 100,
        (counter.getOrDefault(2, 0) / (double) count) * 100,
        (counter.getOrDefault(1, 0) / (double) count) * 100
    );
  }

  public void add(Answer<T> answer) {
    count++;
    answers.addFirst(answer);
    counter.merge(answer.getChoice(), 1, Integer::sum);
  }

  public void addAll(List<Answer> answers) {
    answers.forEach(this::add);
  }

  public List<Answer> getHistory(int limit) {
    return answers.stream().limit(limit).collect(Collectors.toList());
  }

  public T getQuestion() {
    return question;
  }
}
