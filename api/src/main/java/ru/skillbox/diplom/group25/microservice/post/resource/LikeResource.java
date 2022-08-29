package ru.skillbox.diplom.group25.microservice.post.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeDto;

/**
 * LikeResource
 *
 * @author alex90bar
 */

@RequestMapping("api/v1/post/likes")
public interface LikeResource {

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  void create(@RequestBody LikeDto dto);

  @DeleteMapping
  @ResponseStatus(HttpStatus.CREATED)
  void delete(@RequestBody LikeDto dto);

}
