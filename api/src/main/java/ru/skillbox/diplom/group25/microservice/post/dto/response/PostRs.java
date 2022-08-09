package ru.skillbox.diplom.group25.microservice.post.dto.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;

/**
 * PostRs
 *
 * @author alex90bar
 */

@Data
@RequiredArgsConstructor
public class PostRs {

  private Long timestamp;
  private Integer page;
  private Integer size;
  private Integer total;
  private PostDto data;

}


