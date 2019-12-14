package edu.vrgroup.model;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User implements Serializable, Comparable<User> {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @SerializedName("id")
  private Integer id;

  @Column
  @SerializedName("name")
  private String name;

  @Column
  @SerializedName("email")
  private String email;

  @Column
  @SerializedName("gender")
  private String gender;

  @Column
  @SerializedName("age")
  private Integer age;

  public User(String name, String email, String gender, Integer age) {
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

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("id", id)
        .add("name", name)
        .add("email", email)
        .add("gender", gender)
        .add("age", age)
        .toString();
  }
}
