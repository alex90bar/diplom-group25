package ru.skillbox.diplom.group25.microservice.post.service;

import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.between;
import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.equal;
import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.in;
import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.like;

import java.net.URI;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.criteria.Join;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ru.skillbox.diplom.group25.library.core.util.TokenUtil;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeType;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.search.PostSearchDto;
import ru.skillbox.diplom.group25.microservice.post.exception.PostNotFoundException;
import ru.skillbox.diplom.group25.microservice.post.mapper.PostMapper;
import ru.skillbox.diplom.group25.microservice.post.model.Post;
import ru.skillbox.diplom.group25.microservice.post.model.Post_;
import ru.skillbox.diplom.group25.microservice.post.model.Tag;
import ru.skillbox.diplom.group25.microservice.post.model.Tag_;
import ru.skillbox.diplom.group25.microservice.post.repository.LikeRepository;
import ru.skillbox.diplom.group25.microservice.post.repository.PostRepository;
import ru.skillbox.diplom.group25.microservice.post.repository.TagRepository;

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
  private final LikeRepository likeRepository;
  private final TagRepository tagRepository;
  private final PostMapper postMapper;

  @Value(value = "${rest.websocket_uri}")
  private String socketUri;

  @Transactional(readOnly = true)
  public PostDto getById(Long id) {
    log.info("getById begins, id: {}", id);

    Long userId = TokenUtil.getJwtInfo().getId();

    Post post = postRepository.findById(id)
        .orElseThrow(PostNotFoundException::new);

    if (post.getIsDelete()){
      log.info("Post was deleted!");
      throw new PostNotFoundException();
    }

    // проверяем, ставил ли лайк текущий юзер
    log.info("likeRepository.existsByAuthorIdAndTypeAndItemId begins with userId: {} postId: {} ", userId, id);
    Boolean myLike = likeRepository.existsByAuthorIdAndTypeAndItemId(userId, LikeType.POST, id);

    String[] tags =  post.getTagsToPost().stream().map(Tag::getTag).toArray(String[]::new);
    log.info("getById ends ");
    return postMapper.toDto(post, myLike, tags);
  }

  @Transactional(readOnly = true)
  public Page<PostDto> getAll(PostSearchDto searchDto, Pageable page) {
    log.info("getAll begins " + searchDto);
    if (searchDto.getWithFriends() != null && searchDto.getWithFriends()) {
    } //TODO идем в друзья и получаем спискок id друзей и добавляем;

    Long userId = TokenUtil.getJwtInfo().getId();

    return postRepository.findAll(getSpecification(searchDto), page).map(post -> {

      // проверяем, ставил ли лайк текущий юзер
      log.info("likeRepository.existsByAuthorIdAndTypeAndItemId begins with userId: {} postId: {}", userId, post.getId());
      Boolean myLike = likeRepository.existsByAuthorIdAndTypeAndItemId(userId, LikeType.POST, post.getId());

      String[] tags =  post.getTagsToPost().stream().map(Tag::getTag).toArray(String[]::new);

      return postMapper.toDto(post, myLike, tags);
    });
  }

  public void create(PostDto dto) {
    log.info("create begins post {}", dto);
    dto.setAuthorId(TokenUtil.getJwtInfo().getId());


    // обрабатываем теги, если теги новые - создаем, если теги уже имеются - закрепляем их за постом.
    String[] tags = dto.getTags();
    Set<Tag> tagSet = new HashSet<>();
    if (tags != null){
      for (String tag : tags){
        Tag existingTag = tagRepository.findByTag(tag);

        if (existingTag == null){
          Tag newTag = new Tag();
          newTag.setTag(tag);
          tagSet.add(tagRepository.save(newTag));
        } else {
          tagSet.add(existingTag);
        }
      }
    }

    postRepository.save(postMapper.toEntity(dto)).setTagsToPost(tagSet);


      try {
        WebSocketClient webSocketClient = new StandardWebSocketClient();

        final WebSocketHttpHeaders headers = new WebSocketHttpHeaders();
        headers.add("Authorization", "Bearer " + "eyJhbGciOiJIUzI1NiJ9.eyJmaXJzdE5hbWUiOiLQkNC70LXQutGB0LDQ"
            + "vdC00YAiLCJpZCI6NiwiZXhwIjoxOTc4MzYzNTA3LCJlbWFpbCI6ImFsZXg5MGJhckBnbWFpbC5jb20iLCJyb2xlcyI6IlVTRVIifQ.SrWNrjcmNX"
            + "3l4HCuhvSL44IvNlg9MsWAW4vebK20y-4");

        WebSocketSession webSocketSession = webSocketClient.doHandshake(new TextWebSocketHandler() {
          @Override
          public void handleTextMessage(WebSocketSession session, TextMessage message) {
            log.info("received message - " + message.getPayload());
          }

          @Override
          public void afterConnectionEstablished(WebSocketSession session) {
            log.info("established connection - " + session);
          }
        }, headers, URI.create(socketUri)).get();


          try {
            TextMessage message = new TextMessage("Testing web socket message!");
            webSocketSession.sendMessage(message);
            log.info("sent message - " + message.getPayload());
          } catch (Exception e) {
            log.error("Exception while sending a message", e);
          }


    }  catch (Exception e) {
        log.error("Exception while accessing websockets", e);
      }

    log.info("create ends");
  }

  public void update(PostDto dto, Long id) {
    log.info("update begins post {}", dto);
    dto.setId(id);
    Post post = postRepository.findById(dto.getId())
        .orElseThrow(PostNotFoundException::new);

    Long userId = TokenUtil.getJwtInfo().getId();

    if (!post.getAuthorId().equals(userId)){
      log.error("Cannot update post of another user (post.authorId is not equal userId)");
      return;
    }

    // обрабатываем теги, если теги новые - создаем, если теги уже имеются - закрепляем их за постом.
    String[] tags = dto.getTags();
    Set<Tag> tagSet = new HashSet<>();

    if (tags != null){
      for (String tag : tags){
        Tag existingTag = tagRepository.findByTag(tag);

        if (existingTag == null){
          Tag newTag = new Tag();
          newTag.setTag(tag);
          tagSet.add(tagRepository.save(newTag));
        } else {
          tagSet.add(existingTag);
        }
      }
    }

    post.setTagsToPost(tagSet);

    postMapper.updatePostFromDto(dto, post);
    log.info("update ends");
  }

  public void deleteById(Long id) {
    log.info("deleteById begins");
    Post post = postRepository.findById(id)
        .orElseThrow(PostNotFoundException::new);

    Long userId = TokenUtil.getJwtInfo().getId();

    if (!post.getAuthorId().equals(userId)){
      log.error("Cannot delete post of another user (post.authorId is not equal userId)");
      return;
    }

    post.setIsDelete(true);
    log.info("deleteById ends");
  }



  public ZonedDateTime secondsToZoned(Long seconds){
    if (seconds == null){
      return null;
    } else {
      return ZonedDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault());
    }
  }

  public Specification<Post> getSpecification(PostSearchDto dto) {
    log.info("Datefrom: {} dateto: {}", secondsToZoned(dto.getDate_from()), secondsToZoned(dto.getDate_to()));
    return in(Post_.id, dto.getIds(), true)
        .and(containsTag(dto.getTags()))
        .and(between(Post_.time, secondsToZoned(dto.getDate_from()), secondsToZoned(dto.getDate_to()), true))
        .and(like(Post_.postText, dto.getPostText(), true))
        .and(like(Post_.title, dto.getTitle(), true))
        .and(equal(Post_.isDelete, dto.getIsDelete(), true))
        .and(in(Post_.authorId, dto.getAccountIds(), true));

  }

  public Specification<Post> containsTag(String[] tags){
    return (root, query, builder) -> {
      if (tags == null) return builder.conjunction();
      Join<Post, Tag> join = root.join(Post_.TAGS_TO_POST);
      return builder.in(join.get(Tag_.TAG)).value(Arrays.asList(tags));
    };
  }




  public void dislike(Long itemId) {
    log.info("dislike begins, postId: {}", itemId);
    Post post = postRepository.findById(itemId)
        .orElseThrow(PostNotFoundException::new);
    post.setLikeAmount(post.getLikeAmount() - 1);
    log.info("dislike ends");
  }

  public void setLike(Long itemId) {
    log.info("setLike begins, postId: {}", itemId);
    Post post = postRepository.findById(itemId)
        .orElseThrow(PostNotFoundException::new);
    post.setLikeAmount(post.getLikeAmount() + 1);
    log.info("setLike ends");
  }
}

