package ru.skillbox.diplom.group25.microservice.post.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeDto;
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
  public void create(LikeDto dto) {
    likeService.create(dto);
  }
}


