package ru.skillbox.diplom.group25.microservice.post.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * CountryDto
 *
 * @author alex90bar
 */

@Data
@RequiredArgsConstructor
public class CountryDto {

  private Integer id;
  private String title;

}


