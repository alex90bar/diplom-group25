package ru.skillbox.diplom.group25.microservice.post.dto;

import java.time.ZonedDateTime;
import java.util.List;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * PostDto
 *
 * @author alex90bar
 */

@Data
@RequiredArgsConstructor
public class PostDto {

  private Long id;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private ZonedDateTime time; //TODO измени если это время публикации
  private Long author;
  private String title;
  private PostType type;
  private String postText;
  private Boolean isBlocked;
  private List<CommentDto> comments;
  private List<PostTagDto> tags;
  private Integer likes;
  private Boolean myLike;
  private String photoUrl;
  private ZonedDateTime publishDate;



}


