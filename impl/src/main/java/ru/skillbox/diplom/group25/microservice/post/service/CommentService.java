package ru.skillbox.diplom.group25.microservice.post.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentType;
import ru.skillbox.diplom.group25.microservice.post.mapper.CommentMapper;
import ru.skillbox.diplom.group25.microservice.post.model.Comment;
import ru.skillbox.diplom.group25.microservice.post.model.Post;
import ru.skillbox.diplom.group25.microservice.post.repository.CommentRepository;
import ru.skillbox.diplom.group25.microservice.post.repository.PostRepository;

/**
 * CommentService
 *
 * @author alex90bar
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final PostRepository postRepository;
  private final CommentMapper mapper;

  public void create(CommentDto dto) {
    log.info("create begins comment " + dto);
    postRepository.findById(dto.getPostId())
        .orElseThrow(() -> new NotFoundException("Post not found with id: " + dto.getPostId()));
    //если это комент на комент, проверяем наличие родительского комента в БД
    if (dto.getParentId() != null) {
      if (!commentRepository.existsById(dto.getParentId()))
        throw new NotFoundException("Comment not found with parentId: " + dto.getParentId());
    }
    commentRepository.save(mapper.toEntity(dto));
    log.info("create ends");
  }

  @Transactional(readOnly = true)
  public List<CommentDto> getAllByPostId(Long id) {
    log.info("getAllByPostId begins, postId " + id);
    List<CommentDto> comments = commentRepository.findAllByPostIdOrderByIdAsc(id)
        .stream().map(mapper::toDto).collect(Collectors.toList());
    //получаем все комменты на комменты
    getChildComments(comments);
    //удаляем дубликаты
    return comments.stream()
        .filter(c -> c.getCommentType().equals(CommentType.POST))
        .collect(Collectors.toList());
  }

  private void getChildComments(List<CommentDto> comments){
    for (int i = 0; i < comments.size(); i++){
      CommentDto dto = comments.get(i);
      List<CommentDto> subComments = comments.stream()
          .filter(c -> c.getParentId().equals(dto.getId()))
          .collect(Collectors.toList());
      getChildComments(subComments);
      dto.setSubComments(subComments);
    }
  }

  public void deleteById(Long id) {
    log.info("deleteById begins");
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Comment not found with id: " + id));
    comment.setIsDelete(true);
    log.info("deleteById ends");
  }


  public void update(CommentDto dto) {
    log.info("update begins comment " + dto);
    Comment comment = commentRepository.findById(dto.getId())
        .orElseThrow(() -> new NotFoundException("Comment not found with id: " + dto.getId()));
    mapper.updateCommentFromDto(dto, comment);
    log.info("update ends");
  }

  public void dislike(Long itemId) {
    log.info("dislike begins, commentId: " + itemId);
    Comment comment = commentRepository.findById(itemId)
        .orElseThrow(() -> new NotFoundException("Comment not found with id: " + itemId));;
    comment.setLikeAmount(comment.getLikeAmount() - 1);
    log.info("dislike ends");
  }

  public void setLike(Long itemId) {
    log.info("setLike begins, commentId: " + itemId);
    Comment comment = commentRepository.findById(itemId)
        .orElseThrow(() -> new NotFoundException("Comment not found with id: " + itemId));;
    comment.setLikeAmount(comment.getLikeAmount() + 1);
    log.info("setLike ends");
  }
}


