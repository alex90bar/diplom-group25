package ru.skillbox.diplom.group25.microservice.post.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * AccountByIdDto
 *
 * @author alex90bar
 */

@Data
@RequiredArgsConstructor
public class AccountByIdDto {

  private Long id;
  private String email;
  private String phone;
  private String photo;
  private String about;
  private String city;
  private String country;

  private Object statusCode = null;
  private String firstName;
  private String lastName;
  private Long regDate;
  private Long birthDate;

 // private MessagePermission messagePermission;
  private String messagePermission;
  private Long lastOnlineTime;
  private Boolean isOnline;
  private Boolean isBlocked;

  private Boolean isDeleted;
  private String message;


}


