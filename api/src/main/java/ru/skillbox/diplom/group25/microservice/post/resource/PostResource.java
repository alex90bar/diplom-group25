package ru.skillbox.diplom.group25.microservice.post.resource;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.search.PostSearchDto;

/**
 * PostResource
 *
 * @author alex90bar
 */

//TODO Добавь реквест маппинг с api/v1/post/
public interface PostResource {

  @GetMapping("api/v1/post/{id}")
  ResponseEntity<PostDto> getById(@PathVariable("id") Long id);

  @GetMapping("api/v1/post")
  ResponseEntity<Page<PostDto>> getAll(PostSearchDto searchDto, Pageable page);

  @PostMapping("api/v1/post/") //TODO а здесь убери api/v1/post/
  @ResponseStatus(HttpStatus.CREATED)
  void create(@RequestBody PostDto dto);

  @PutMapping("api/v1/post/")
  @ResponseStatus(HttpStatus.CREATED)
  void update(@RequestBody PostDto dto);

  @DeleteMapping("api/v1/post/{id}")
  @ResponseStatus(HttpStatus.OK)
  void deleteById(@PathVariable("id") Long id);

}
