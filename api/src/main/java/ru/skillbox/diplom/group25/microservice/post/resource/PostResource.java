package ru.skillbox.diplom.group25.microservice.post.resource;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.request.PostAddRq;
import ru.skillbox.diplom.group25.microservice.post.dto.request.PostSearchDto;
import ru.skillbox.diplom.group25.microservice.post.dto.response.PostRs;

/**
 * PostResource
 *
 * @author alex90bar
 */

//TODO Добавь реквест маппинг с api/v1/post/
public interface PostResource {

  @PostMapping("api/v1/post/") //TODO а здесь убери api/v1/post/
  @ResponseStatus(HttpStatus.CREATED)
  void create(@RequestParam(value = "publish_date",
      required = false) String publishDate,
      @RequestBody PostAddRq postAddRq);

  @GetMapping("api/v1/post/{id}")
  PostRs getById(@PathVariable("id") String id);

  @DeleteMapping("api/v1/post/{id}")
  PostRs deleteById(@PathVariable("id") String id);

  @GetMapping("api/v1/post/search")
  List<PostDto> search(@RequestBody PostSearchDto searchDto);

}
