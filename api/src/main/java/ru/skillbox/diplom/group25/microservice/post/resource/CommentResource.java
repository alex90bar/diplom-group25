package ru.skillbox.diplom.group25.microservice.post.resource;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

  @PostMapping("/{id}/comment")
  @ResponseStatus(HttpStatus.CREATED)
  void create(@RequestBody CommentDto dto, @PathVariable("id") Long id);

  @GetMapping("/{id}/comment")
  ResponseEntity<Page<CommentDto>> getByPostId(@PathVariable("id") Long id, Pageable page);

  @GetMapping("/{id}/comment/{commentId}/subcomment")
  ResponseEntity<Page<CommentDto>> getByPostIdAndCommentId(@PathVariable("id") Long id,
      @PathVariable("commentId") Long commentId, Pageable page);

  @DeleteMapping("/{id}/comment")
  @ResponseStatus(HttpStatus.OK)
  void deleteById(@PathVariable("id") Long id);

  @PutMapping("/comment")
  @ResponseStatus(HttpStatus.CREATED)
  void update(@RequestBody CommentDto dto);
}
