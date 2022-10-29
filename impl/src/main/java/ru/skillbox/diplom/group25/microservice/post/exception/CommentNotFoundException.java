package ru.skillbox.diplom.group25.microservice.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * CommentNotFoundException
 *
 * @author alex90bar
 */

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = CommentNotFoundException.REASON_RU)
public class CommentNotFoundException extends RuntimeException {

  public static final String REASON_RU = "Комментарий не найден в БД";
  public static final String REASON_EN = "Comment not found in DB";

}


