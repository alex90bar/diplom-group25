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
public class PostCommentDto {

  private Integer id;
  private Long time;
  private AccountByIdDto author;
  private Integer parentId;
  private String commentText;
  private Integer postId;
  private Boolean isBlocked;
  private Integer likes;
  private Boolean myLike;
  private String subComments;

}


