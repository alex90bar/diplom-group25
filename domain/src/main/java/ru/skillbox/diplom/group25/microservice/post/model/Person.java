package ru.skillbox.diplom.group25.microservice.post.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * Person
 *
 * @author alex90bar
 */
@Data
@Table(name = "person")
@Entity
public class Person {

  @Id
  @Column(name = "id", nullable = false)
  private Long id;

}


