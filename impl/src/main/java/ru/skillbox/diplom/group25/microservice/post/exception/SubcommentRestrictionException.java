package ru.skillbox.diplom.group25.microservice.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * SubcommentRestrictionException
 *
 * @author alex90bar
 */

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = PostNotFoundException.REASON_RU)
public class SubcommentRestrictionException extends RuntimeException {

  public static final String REASON_RU = "Невозможно создать комментарий к подкомментарию";
  public static final String REASON_EN = "Cannot create subsubcomment for comment";

}


