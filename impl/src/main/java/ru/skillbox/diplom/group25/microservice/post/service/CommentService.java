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
import ru.skillbox.diplom.group25.library.core.util.TokenUtil;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentType;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeType;
import ru.skillbox.diplom.group25.microservice.post.mapper.CommentMapper;
import ru.skillbox.diplom.group25.microservice.post.model.Comment;
import ru.skillbox.diplom.group25.microservice.post.model.Post;
import ru.skillbox.diplom.group25.microservice.post.repository.CommentRepository;
import ru.skillbox.diplom.group25.microservice.post.repository.LikeRepository;
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
  private final LikeRepository likeRepository;

  public void create(CommentDto dto, Long id) {
    log.info("create begins comment " + dto);

    //проверяем наличие поста в базе
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

    //если это комент на комент, проверяем наличие родительского комента в БД и ограничиваем создание коммента на коммент 2 уровнями
    if (dto.getParentId() != null) {
      Comment comment = commentRepository.findById(dto.getParentId())
          .orElseThrow(() -> new NotFoundException("Comment not found with parentId: " + dto.getParentId()));
      if (comment.getParentId() > 0){
        throw new NotFoundException("Cannot create subsubcomment for comment");
      }
      comment.setCommentsCount(comment.getCommentsCount() + 1);
    }
    dto.setAuthorId(TokenUtil.getJwtInfo().getId());
    dto.setPostId(id);
    dto.setCommentsCount(0);

    if (dto.getParentId() == null){
      post.setCommentsCount(post.getCommentsCount() + 1);
    }

    commentRepository.save(mapper.toEntity(dto));

    log.info("create ends");
  }

  @Transactional(readOnly = true)
  public List<CommentDto> getAllByPostIdAndCommentId(Long id, Long commentId, Pageable page) {
    log.info("getAllByPostId begins, postId {}", id);

    Long userId = TokenUtil.getJwtInfo().getId();

    return commentRepository.findAllByPostIdAndParentId(id, commentId, page)
        .stream().map(comment -> {

          // проверяем каждый комент на наличие лайка от текущего юзера
          log.info("likeRepository.existsByAuthorIdAndTypeAndItemId begins with userId: {}  commentId: {}", userId, comment.getId());
          Boolean myLike = likeRepository.existsByAuthorIdAndTypeAndItemId(userId, LikeType.COMMENT, comment.getId());
          return mapper.toDto(comment, myLike);
        }).collect(Collectors.toList());

  }

  public void deleteById(Long id) {
    log.info("deleteById begins");
    Comment comment = commentRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Comment not found with id: " + id));
    comment.setIsDelete(true);
    log.info("deleteById ends");
  }


  public void update(CommentDto dto) {
    log.info("update begins comment {}", dto);
    Comment comment = commentRepository.findById(dto.getId())
        .orElseThrow(() -> new NotFoundException("Comment not found with id: " + dto.getId()));
    mapper.updateCommentFromDto(dto, comment);
    log.info("update ends");
  }

  public void dislike(Long itemId) {
    log.info("dislike begins, commentId: {} ", itemId);
    Comment comment = commentRepository.findById(itemId)
        .orElseThrow(() -> new NotFoundException("Comment not found with id: " + itemId));
    comment.setLikeAmount(comment.getLikeAmount() - 1);
    log.info("dislike ends");
  }

  public void setLike(Long itemId) {
    log.info("setLike begins, commentId: {}", itemId);
    Comment comment = commentRepository.findById(itemId)
        .orElseThrow(() -> new NotFoundException("Comment not found with id: " + itemId));
    comment.setLikeAmount(comment.getLikeAmount() + 1);
    log.info("setLike ends");
  }


}


