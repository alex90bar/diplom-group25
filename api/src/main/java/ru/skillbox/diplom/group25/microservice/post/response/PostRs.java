package ru.skillbox.diplom.group25.microservice.post.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

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
  private String data;  //TODO data = PostDto

}


