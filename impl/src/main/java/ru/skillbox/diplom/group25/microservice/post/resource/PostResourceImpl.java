package ru.skillbox.diplom.group25.microservice.post.resource;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group25.microservice.post.client.PostFeignClient;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.request.PostAddRq;
import ru.skillbox.diplom.group25.microservice.post.dto.request.PostSearchDto;
import ru.skillbox.diplom.group25.microservice.post.dto.response.PostRs;
import ru.skillbox.diplom.group25.microservice.post.service.PostService;

/**
 * PostResourceImpl
 *
 * @author alex90bar
 */

@RestController
@RequiredArgsConstructor
public class PostResourceImpl implements PostResource {

  private final PostService postService;
  private final PostFeignClient feignClient;

  @Override
  public void create(String publishDate, PostAddRq postAddRq) {
    postService.create(publishDate, postAddRq);
  }

  @Override
  public PostRs getById(String id) {
    return postService.getById(id);
  }

  @Override
  public PostRs deleteById(String id) {
    return postService.deleteById(id);
  }

  @Override
  public List<PostDto> search(PostSearchDto searchDto) {
    return postService.searchByDto(searchDto);
  }
}


