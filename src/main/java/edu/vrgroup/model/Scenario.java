package edu.vrgroup.model;

import com.google.gson.annotations.Expose;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Scenario implements Serializable, Comparable<Scenario> {

  public static Scenario DEFAULT;

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
    return id.equals(scenario.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return name;
  }

  @Override
  public int compareTo(Scenario other) {
    return this.name.compareTo(other.name);
  }
}
