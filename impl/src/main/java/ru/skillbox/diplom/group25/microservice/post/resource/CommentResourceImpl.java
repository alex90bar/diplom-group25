package ru.skillbox.diplom.group25.microservice.post.resource;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
  public ResponseEntity<Page<CommentDto>> getByPostId(Long id, Pageable page) {
    return ResponseEntity.ok(commentService.getAllByPostIdAndCommentId(id, 0L, page));
  }

  @Override
  public ResponseEntity<Page<CommentDto>> getByPostIdAndCommentId(Long id, Long commentId, Pageable page) {
    return ResponseEntity.ok(commentService.getAllByPostIdAndCommentId(id, commentId, page));
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


