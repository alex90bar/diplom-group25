package ru.skillbox.diplom.group25.microservice.post.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Post
 *
 * @author alex90bar
 */

@Getter
@Setter
@Entity
@Table(name = "post")
@RequiredArgsConstructor
public class Post {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "time", nullable = false)
  private ZonedDateTime time;

  @Column(name = "author_id", nullable = false)
  private Long authorId;

  @Column(name = "title", nullable = false)
  private String title;

  @Column(name = "post_text", nullable = false)
  private String postText;

  @Column(name = "is_blocked")
  private Boolean isBlocked;

  @Column(name = "is_delete")
  private Boolean isDelete;

  @Column(name = "like_amount", nullable = false)
  private Integer likeAmount;

  @Column(name = "comments_count", nullable = false)
  private Integer commentsCount;

  @Column(name = "my_like")
  private Boolean myLike;

  @Column(name = "image_path", nullable = false)
  private String imagePath;

  @Column(name = "publish_date")
  private ZonedDateTime publishDate;

}


