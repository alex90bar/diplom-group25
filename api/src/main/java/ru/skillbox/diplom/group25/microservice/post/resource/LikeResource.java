package ru.skillbox.diplom.group25.microservice.post.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * LikeResource
 *
 * @author alex90bar
 */

@RequestMapping("api/v1/post")
public interface LikeResource {

  @PostMapping("/{id}/like")
  @ResponseStatus(HttpStatus.CREATED)
  void create(@PathVariable("id") Long id);

  @DeleteMapping("/{id}/like")
  @ResponseStatus(HttpStatus.CREATED)
  void delete(@PathVariable("id") Long id);

  @PostMapping("/{id}/comment/{commentId}/like")
  @ResponseStatus(HttpStatus.CREATED)
  void createToComment(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId);

  @DeleteMapping("/{id}/comment/{commentId}/like")
  @ResponseStatus(HttpStatus.CREATED)
  void deleteToComment(@PathVariable("id") Long id, @PathVariable("commentId") Long commentId);

}
