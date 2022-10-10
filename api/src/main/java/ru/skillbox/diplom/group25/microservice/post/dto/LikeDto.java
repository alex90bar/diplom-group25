package ru.skillbox.diplom.group25.microservice.post.dto;

import java.time.ZonedDateTime;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * LikeDto
 *
 * @author alex90bar
 */

@Data
@RequiredArgsConstructor
public class LikeDto {

  private Long id;
  private Long authorId;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private ZonedDateTime time;
  private Long itemId;
  private LikeType type;

}


