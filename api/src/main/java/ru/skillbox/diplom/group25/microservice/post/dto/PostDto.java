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
  private ZonedDateTime time;
  private Long authorId;
  private String title;
  private PostType type;
  private String postText;
  private Boolean isBlocked;
  private Boolean isDelete;
  private Integer commentsCount;
  private String[] tags;
  private Integer likeAmount;
  private Boolean myLike;
  private String imagePath;
  private ZonedDateTime publishDate;



}


