package ru.skillbox.diplom.group25.microservice.post.model;

import java.time.ZonedDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Comment
 *
 * @author alex90bar
 */

@Getter
@Setter
@Entity
@Table(name = "comment")
@RequiredArgsConstructor
public class Comment {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "time", nullable = false)
  private ZonedDateTime time;

  @Column(name = "parent_id", nullable = false)
  private Long parentId;

  @Column(name = "comment_text", nullable = false)
  private String commentText;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "post_id", referencedColumnName = "id")
  private Post post;

  @Column(name = "author_id", nullable = false)
  private Long authorId;

  @Column(name = "is_blocked")
  private Boolean isBlocked;

  @Column(name = "is_delete")
  private Boolean isDelete;

  @Column(name = "comments_count", nullable = false)
  private Integer commentsCount;

  @Column(name = "like_amount", nullable = false)
  private Integer likeAmount;

  @Column(name = "my_like")
  private Boolean myLike;

  @Column(name = "image_path", nullable = false)
  private String imagePath;

}


