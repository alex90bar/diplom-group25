package ru.skillbox.diplom.group25.microservice.post.service;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.skillbox.diplom.group25.library.core.util.TokenUtil;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentType;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeType;
import ru.skillbox.diplom.group25.microservice.post.dto.NotificationInputDto;
import ru.skillbox.diplom.group25.microservice.post.dto.NotificationType;
import ru.skillbox.diplom.group25.microservice.post.exception.CommentNotFoundException;
import ru.skillbox.diplom.group25.microservice.post.exception.PostNotFoundException;
import ru.skillbox.diplom.group25.microservice.post.exception.SubcommentRestrictionException;
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
  private final KafkaSender kafkaSender;

  @Value(value = "${kafka-topics.notifications}")
  private String topicNotification;


  /**
   * Отправка нотификации по комментарию
   * */
  private void sendNotification(Long authorId, Long userId, NotificationType type, String content){

    String text = content.length() > 20 ? content.substring(0, 20) + "..." : content;

    NotificationInputDto notification = new NotificationInputDto(authorId, userId,
        type, text);

    kafkaSender.sendMessage(topicNotification, "New post notification", notification);
  }

  /**
   * Создание нового комментария
   * */
  public void create(CommentDto dto, Long id) {
    log.info("create begins comment {}", dto);

    //проверяем наличие поста в базе
    Post post = postRepository.findById(id)
        .orElseThrow(PostNotFoundException::new);

    dto.setAuthorId(TokenUtil.getJwtInfo().getId());

    //если это комент на комент, проверяем наличие родительского комента в БД и ограничиваем создание коммента на коммент 2 уровнями
    if (dto.getParentId() != null) {
      Comment comment = commentRepository.findById(dto.getParentId())
          .orElseThrow(CommentNotFoundException::new);
      if (comment.getParentId() > 0){
        throw new SubcommentRestrictionException();
      }

      //пересчитываем количество подкомментов к коменту, отправляем нотификацию автору комента о новом подкоменте
      comment.setCommentsCount(comment.getCommentsCount() + 1);

      sendNotification(dto.getAuthorId(), comment.getAuthorId(), NotificationType.COMMENT_COMMENT, dto.getCommentText());
    }

    dto.setPostId(id);
    dto.setCommentsCount(0);

    //если это коммент к посту - пересчитываем количество комментов к посту, отправляем нотификацию автору поста о новом коменте
    if (dto.getParentId() == null){
      post.setCommentsCount(post.getCommentsCount() + 1);

      sendNotification(dto.getAuthorId(), post.getAuthorId(), NotificationType.POST_COMMENT, dto.getCommentText());
    }

    commentRepository.save(mapper.toEntity(dto));

    log.info("create ends");
  }

  /**
   * Получение подкомментариев по id поста и комментария
   * */
  @Transactional(readOnly = true)
  public Page<CommentDto> getAllByPostIdAndCommentId(Long id, Long commentId, Pageable page) {
    log.info("getAllByPostId begins, postId {}", id);

    Long userId = TokenUtil.getJwtInfo().getId();

    return commentRepository.findAllByPostIdAndParentIdAndIsDelete(id, commentId, false, page)
        .map(comment -> {

          // проверяем каждый комент на наличие лайка от текущего юзера
          log.info("likeRepository.existsByAuthorIdAndTypeAndItemId begins with userId: {}  commentId: {}", userId, comment.getId());
          Boolean myLike = likeRepository.existsByAuthorIdAndTypeAndItemId(userId, LikeType.COMMENT, comment.getId());
          return mapper.toDto(comment, myLike);
        });

  }

  /**
   * Удаление комментария по id
   * */
  public void deleteById(Long commentId) {
    log.info("deleteById begins");
    Comment comment = commentRepository.findById(commentId)
        .orElseThrow(CommentNotFoundException::new);

    Long userId = TokenUtil.getJwtInfo().getId();

    if (!comment.getAuthorId().equals(userId)){
      log.error("Cannot delete comment of another user (comment.authorId is not equal userId)");
      return;
    }

    comment.setIsDelete(true);

    //пересчитываем количество комментов к посту / родительскому комменту
    if (comment.getParentId().equals(0L)){
      Post post = postRepository.findById(comment.getPost().getId())
          .orElseThrow(PostNotFoundException::new);
      post.setCommentsCount(post.getCommentsCount() - 1);
    } else {
      Comment parentComment = commentRepository.findById(comment.getParentId())
          .orElseThrow(CommentNotFoundException::new);
      parentComment.setCommentsCount(parentComment.getCommentsCount() - 1);
    }


    log.info("deleteById ends");
  }

  /**
   *    * Редактирование комментария по id через dto
   * */
  public void update(CommentDto dto, Long commentId) {
    log.info("update begins comment {}", dto);
    dto.setId(commentId);

    Comment comment = commentRepository.findById(dto.getId())
        .orElseThrow(CommentNotFoundException::new);

    Long userId = TokenUtil.getJwtInfo().getId();

    if (!comment.getAuthorId().equals(userId)){
      log.error("Cannot update comment of another user (comment.authorId is not equal userId)");
      return;
    }

    //обновляем дату публикации комментария
    dto.setTimeChanged(ZonedDateTime.now());

    mapper.updateCommentFromDto(dto, comment);
    log.info("update ends");
  }

  /**
   * Метод удаления лайка (пересчет количества лайков коммента)
   * */
  public void dislike(Long itemId) {
    log.info("dislike begins, commentId: {} ", itemId);
    Comment comment = commentRepository.findById(itemId)
        .orElseThrow(CommentNotFoundException::new);
    comment.setLikeAmount(comment.getLikeAmount() - 1);
    log.info("dislike ends");
  }

  /**
   * Метод добавления лайка (пересчет количества лайков коммента)
   * */
  public void setLike(Long itemId) {
    log.info("setLike begins, commentId: {}", itemId);
    Comment comment = commentRepository.findById(itemId)
        .orElseThrow(CommentNotFoundException::new);
    comment.setLikeAmount(comment.getLikeAmount() + 1);
    log.info("setLike ends");
  }


}


