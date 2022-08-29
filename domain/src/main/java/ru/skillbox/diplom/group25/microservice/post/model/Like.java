package ru.skillbox.diplom.group25.microservice.post.model;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeType;

/**
 * Like
 *
 * @author alex90bar
 */

@Getter
@Setter
@Entity
@Table(name = "likes")
@RequiredArgsConstructor
public class Like {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "time", nullable = false)
  private ZonedDateTime time;

  @Column(name = "author_id", nullable = false)
  private Long authorId;

  @Column(name = "item_id", nullable = false)
  private Long itemId;

  @Column(name = "type", nullable = false)
  private LikeType type;

}


