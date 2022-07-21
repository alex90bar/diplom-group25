package ru.skillbox.diplom.group25.microservice.post.resource;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.skillbox.diplom.group25.microservice.post.request.PostAddRq;
import ru.skillbox.diplom.group25.microservice.post.response.PostRs;

/**
 * PostResource
 *
 * @author alex90bar
 */

@RequestMapping("api/v1/post/")
public interface PostResource {

  @PostMapping
  void addNewPost(@RequestParam(value = "publish_date",
      defaultValue = "-1",
      required = false) String publishDate,
      @RequestBody PostAddRq postAddRq);

  @GetMapping("{id}")
  PostRs getPostById(@PathVariable String id);

  @DeleteMapping("{id}")
  PostRs deletePostById(@PathVariable String id);

}
