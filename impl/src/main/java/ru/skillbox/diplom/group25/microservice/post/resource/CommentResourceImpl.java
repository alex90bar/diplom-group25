package ru.skillbox.diplom.group25.microservice.post.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
import ru.skillbox.diplom.group25.microservice.post.service.CommentService;

/**
 * CommentResourceImpl
 *
 * @author alex90bar
 */

@RestController
@RequiredArgsConstructor
public class CommentResourceImpl implements CommentResource{

  private final CommentService commentService;


  @Override
  public void create(CommentDto dto, Long id) {
    commentService.create(dto, id);
  }

  @Override
  public void deleteById(Long id) {
    commentService.deleteById(id);
  }

  @Override
  public void update(CommentDto dto) {
    commentService.update(dto);
  }

}


