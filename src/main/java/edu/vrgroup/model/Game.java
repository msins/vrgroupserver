package edu.vrgroup.model;

import java.io.Serializable;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity(name = "Game")
public class Game implements Serializable {

  @Id
  private Integer id;

  @Column(name = "name")
  private String name;

  public Game(String name) {
    this.name = name;
    this.id = name.hashCode() ^ ThreadLocalRandom.current().nextInt();
  }

  public Game() {
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    Game game = (Game) o;

    return name.equals(game.name);
  }

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public String toString() {
    return name;
  }
}
