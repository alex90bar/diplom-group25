package ru.skillbox.diplom.group25.microservice.post.dto;

import java.time.ZonedDateTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * PostCommentDto
 *
 * @author alex90bar
 */

@Data
@RequiredArgsConstructor
public class CommentDto {

  private Long id;
  private CommentType commentType;
  private ZonedDateTime time;
  private ZonedDateTime timeChanged;
  private Long authorId;
  private Long parentId;
  private String commentText;
  private Long postId;
  private Boolean isBlocked;
  private Boolean isDelete;
  private Integer likeAmount;
  private Boolean myLike;
  private Integer commentsCount;
  private String imagePath;

}


