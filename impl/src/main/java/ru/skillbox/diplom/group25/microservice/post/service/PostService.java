package ru.skillbox.diplom.group25.microservice.post.service;

import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.in;
import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.like;

import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeType;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.search.PostSearchDto;
import ru.skillbox.diplom.group25.microservice.post.mapper.PostMapper;
import ru.skillbox.diplom.group25.microservice.post.model.Post;
import ru.skillbox.diplom.group25.microservice.post.model.Post_;
import ru.skillbox.diplom.group25.microservice.post.repository.LikeRepository;
import ru.skillbox.diplom.group25.microservice.post.repository.PostRepository;

/**
 * PostService
 *
 * @author alex90bar
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final CommentService commentService;
  private final LikeRepository likeRepository;
  private final PostMapper postMapper;

  @Transactional(readOnly = true)
  public PostDto getById(Long id) {
    log.info("getById begins, id: " + id);

    //TODO: получаем из jwt-токена текущий userid
    Long userId = 3L;

    Post post = postRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
    List<CommentDto> comments = commentService.getAllByPostId(id);

    // проверяем, ставил ли лайк текущий юзер
    log.info("likeRepository.existsByAuthorIdAndTypeAndItemId begins with userId: " + userId + ", postId: " + id);
    Boolean myLike = likeRepository.existsByAuthorIdAndTypeAndItemId(userId, LikeType.POST, id);

    log.info("getById ends ");
    return postMapper.toDto(post, comments, myLike);
  }

  @Transactional(readOnly = true)
  public Page<PostDto> getAll(PostSearchDto searchDto, Pageable page) {
    log.info("getAll begins " + searchDto); //свой id тебе должен передавать фронт
    if (searchDto.getWithFriends() != null && searchDto.getWithFriends()) {
    } //TODO идем в друзья и получаем спискок id друзей и добавляем;

    //TODO: получаем из jwt-токена текущий userid
    Long userId = 3L;

    return postRepository.findAll(getSpecification(searchDto), page).map(post -> {
      List<CommentDto> comments = commentService.getAllByPostId(post.getId());

      // проверяем, ставил ли лайк текущий юзер
      log.info("likeRepository.existsByAuthorIdAndTypeAndItemId begins with userId: " + userId + ", postId: " + post.getId());
      Boolean myLike = likeRepository.existsByAuthorIdAndTypeAndItemId(userId, LikeType.POST, post.getId());

      return postMapper.toDto(post, comments, myLike);
    });
  }

  public void create(PostDto dto) {
    log.info("create begins post " + dto);
    postRepository.save(postMapper.toEntity(dto));
    log.info("create ends");
  }

  public void update(PostDto dto) {
    log.info("update begins post " + dto);
    Post post = postRepository.findById(dto.getId())
        .orElseThrow(() -> new NotFoundException("Post not found with id: " + dto.getId()));
    postMapper.updatePostFromDto(dto, post);
    log.info("update ends");
  }

  public void deleteById(Long id) {
    log.info("deleteById begins");
    Post post = postRepository.findById(id)
        .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));
    postRepository.delete(post);
    log.info("deleteById ends");
  }

  public Specification<Post> getSpecification(PostSearchDto dto) {
    return in(Post_.id, dto.getIds(), true)
        .and(like(Post_.postText, dto.getPostText(), true))
        .and(like(Post_.title, dto.getTitle(), true))
        .and(in(Post_.authorId, dto.getAccountIds(), true));
  }

  public void dislike(Long itemId) {
    log.info("dislike begins, postId: " + itemId);
    Post post = postRepository.findById(itemId)
        .orElseThrow(() -> new NotFoundException("Post not found with id: " + itemId));;
    post.setLikeAmount(post.getLikeAmount() - 1);
    log.info("dislike ends");
  }

  public void setLike(Long itemId) {
    log.info("setLike begins, postId: " + itemId);
    Post post = postRepository.findById(itemId)
        .orElseThrow(() -> new NotFoundException("Post not found with id: " + itemId));;
    post.setLikeAmount(post.getLikeAmount() + 1);
    log.info("setLike ends");
  }
}

