package ru.skillbox.diplom.group25.microservice.post.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.diplom.group25.microservice.account.model.AccountDto;

/**
 * PostDto
 *
 * @author alex90bar
 */

@Data
@RequiredArgsConstructor
public class PostDto {

  private Integer id;
  private Long time;
  private AccountDto author;
  private String title;
  private PostType type;
  private String postText;
  private Boolean isBlocked;
  private PostCommentDto comments;
  private PostTagDto tags;
  private Integer likes;
  private Boolean myLike;
  private String photoUrl;



}


