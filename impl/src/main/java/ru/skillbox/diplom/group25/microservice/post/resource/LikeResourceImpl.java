package ru.skillbox.diplom.group25.microservice.post.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group25.microservice.post.service.LikeService;

/**
 * LikeResourceImpl
 *
 * @author alex90bar
 */

@RestController
@RequiredArgsConstructor
public class LikeResourceImpl implements LikeResource{

  private final LikeService likeService;

  @Override
  public void create(Long id) {
    likeService.create(id, 0L);
  }

  @Override
  public void delete(Long id) {
    likeService.delete(id, 0L);
  }

  @Override
  public void createToComment(Long id, Long commentId) {
    likeService.create(id, commentId);
  }

  @Override
  public void deleteToComment(Long id, Long commentId) {
    likeService.delete(id, commentId);
  }
}


