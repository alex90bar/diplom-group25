package ru.skillbox.diplom.group25.microservice.post.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

/**
 * Post
 *
 * @author alex90bar
 */

@Data
@Table(name = "post")
@Entity
public class Post {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(name = "time", nullable = false)
  private Long time;

  @Column(name = "author_id", nullable = false)
  private Integer authorId;

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

  @Column(name = "my_like")
  private Boolean myLike;

  @Column(name = "image_path", nullable = false)
  private String imagePath;

}


