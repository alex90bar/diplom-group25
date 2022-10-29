package ru.skillbox.diplom.group25.microservice.post.dto.search;

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
  private Boolean withFriends;
  private Boolean isDelete;
  private String[] tags;
  private Long dateFrom;
  private Long dateTo;
}


