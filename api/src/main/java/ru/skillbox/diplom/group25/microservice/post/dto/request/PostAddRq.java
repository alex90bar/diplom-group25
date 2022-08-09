package ru.skillbox.diplom.group25.microservice.post.dto.request;

import lombok.Data;

/**
 * PostRequest
 *
 * @author alex90bar
 */

@Data
public class PostAddRq {

  private String title;
  private String tags;
  private String postText;
  private String photoUrl;

}


