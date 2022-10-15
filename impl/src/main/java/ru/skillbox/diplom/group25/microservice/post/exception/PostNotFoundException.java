package ru.skillbox.diplom.group25.microservice.post.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * PostNotFoundException
 *
 * @author rmamin
 * @version 0.0.1
 * @since 29.07.2022
 */
@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = PostNotFoundException.REASON_RU)
public class PostNotFoundException extends RuntimeException {

    public static final String REASON_RU = "Пост не найден в БД";
    public static final String REASON_EN = "Post not found in DB";

}
