package ru.skillbox.diplom.group25.microservice.post.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group25.microservice.post.model.Post;
import ru.skillbox.diplom.group25.microservice.post.repository.PostRepository;
import ru.skillbox.diplom.group25.microservice.post.request.PostAddRq;
import ru.skillbox.diplom.group25.microservice.post.response.PostRs;
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

  @Override
  public void addNewPost(String publishDate, PostAddRq postAddRq) {
    postService.addNewPost(publishDate, postAddRq);
  }

  @Override
  public PostRs getPostById(String id) {
    return postService.getPostById(id);
  }

  @Override
  public PostRs deletePostById(String id) {
    return postService.deletePostById(id);
  }


}


