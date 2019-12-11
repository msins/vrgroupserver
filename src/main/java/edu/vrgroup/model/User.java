package edu.vrgroup.model;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User implements Serializable, Comparable<User> {

  @Id
  private Integer id;

  @Column
  private String name;

  @Column
  private String email;

  @Column
  private String gender;

  @Column
  private int age;

  public User(String name, String email) {
    this.id = name.hashCode() ^ ThreadLocalRandom.current().nextInt();
    this.name = name;
    this.email = email;
  }

  public User(String name, String email, String gender, int age) {
    this.id = name.hashCode() ^ ThreadLocalRandom.current().nextInt();
    this.name = name;
    this.email = email;
    this.gender = gender;
    this.age = age;
  }

  public User() {
  }

  public Integer getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }

  public String getGender() {
    return gender;
  }

  public int getAge() {
    return age;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    User user = (User) o;
    return id.equals(user.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public int compareTo(User other) {
    return this.name.compareTo(other.name);
  }
}
