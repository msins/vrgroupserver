package edu.vrgroup.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

  @Id
  private Integer id;

  @Column
  private String name;

  @Column
  private String email;

  public User(String name, String email) {
    this.id = name.hashCode();
    this.name = name;
    this.email = email;
  }

  public User() {
  }

  public String getName() {
    return name;
  }

  public String getEmail() {
    return email;
  }
}
