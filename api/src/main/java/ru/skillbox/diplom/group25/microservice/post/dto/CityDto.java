package ru.skillbox.diplom.group25.microservice.post.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * CityDto
 *
 * @author alex90bar
 */

@Data
@RequiredArgsConstructor
public class CityDto {

  private Integer id;
  private String title;

}


