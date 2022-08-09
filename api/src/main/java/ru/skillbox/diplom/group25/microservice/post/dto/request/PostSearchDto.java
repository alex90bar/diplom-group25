package ru.skillbox.diplom.group25.microservice.post.dto.request;

import java.util.List;
import lombok.Data;

/**
 * PostSearchDto
 *
 * @author alex90bar
 */

@Data
public class PostSearchDto {
  private List<Long> ids;
  private List<Long> accountIds;
  private String title;
  private String postText;
}


