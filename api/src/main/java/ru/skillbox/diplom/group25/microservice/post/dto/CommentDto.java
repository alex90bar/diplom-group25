package ru.skillbox.diplom.group25.microservice.post.dto;

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

  private Integer id;
  private CommentType commentType;
  private Long time;
  private Long author;
  private Integer parentId;
  private String commentText;
  private Integer postId;
  private Boolean isBlocked;
  private Integer likes;
  private Boolean myLike;
  private String subComments;

}


