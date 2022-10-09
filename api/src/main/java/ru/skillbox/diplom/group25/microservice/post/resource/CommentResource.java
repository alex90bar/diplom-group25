package ru.skillbox.diplom.group25.microservice.post.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;

/**
 * CommentResource
 *
 * @author alex90bar
 */

@RequestMapping("api/v1/post")
public interface CommentResource {

  @PostMapping("/{id}/comments")
  @ResponseStatus(HttpStatus.CREATED)
  void create(@RequestBody CommentDto dto, @PathVariable("id") Long id);

  @DeleteMapping("/{id}/comments")
  @ResponseStatus(HttpStatus.OK)
  void deleteById(@PathVariable("id") Long id);

  @PutMapping("/comments")
  @ResponseStatus(HttpStatus.CREATED)
  void update(@RequestBody CommentDto dto);
}
