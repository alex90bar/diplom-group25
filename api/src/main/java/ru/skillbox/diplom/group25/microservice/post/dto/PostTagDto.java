package ru.skillbox.diplom.group25.microservice.post.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * PostTagDto
 *
 * @author alex90bar
 */

@Data
@RequiredArgsConstructor
public class PostTagDto {

  private Integer id;
  private String tag;

}


