package edu.vrgroup.model;

import com.google.gson.annotations.Expose;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Scenario {

  public static Scenario DEFAULT = new Scenario("Default");

  @Id
  @Expose
  private Integer id;

  @Column
  @Expose
  private String name;

  public Scenario() {
  }

  public Scenario(String name) {
    this.id = name.hashCode() ^ ThreadLocalRandom.current().nextInt();
    this.name = name;
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

    Scenario scenario = (Scenario) o;

    return name.equals(scenario.name);
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
