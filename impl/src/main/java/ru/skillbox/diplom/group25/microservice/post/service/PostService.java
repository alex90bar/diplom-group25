package ru.skillbox.diplom.group25.microservice.post.service;

import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.between;
import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.equal;
import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.exclude;
import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.in;
import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.like;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
import ru.skillbox.diplom.group25.library.core.configuration.TechnicalUserConfig;
import ru.skillbox.diplom.group25.library.core.util.TokenUtil;
import ru.skillbox.diplom.group25.microservice.account.client.AccountFeignClient;
import ru.skillbox.diplom.group25.microservice.account.model.AccountByFilterDto;
import ru.skillbox.diplom.group25.microservice.account.model.AccountDto;
import ru.skillbox.diplom.group25.microservice.account.model.AccountSearchDto;
import ru.skillbox.diplom.group25.microservice.friend.client.FriendsFeignClient;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeType;
import ru.skillbox.diplom.group25.microservice.post.dto.NotificationInputDto;
import ru.skillbox.diplom.group25.microservice.post.dto.NotificationType;
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
  private final AccountFeignClient accountFeignClient;
  private final FriendsFeignClient friendsFeignClient;
  private final TechnicalUserConfig technicalUserConfig;
  private final KafkaSender kafkaSender;

  @Value(value = "${kafka-topics.notifications}")
  private String topicNotification;


  /**
   * Получение поста по id
   * */
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

  /**
   * Получение постов по заданным параметрам через searchDto
   * */
  @Transactional(readOnly = true)
  public Page<PostDto> getAll(PostSearchDto searchDto, Pageable page) {
    log.info("getAll begins {}", searchDto);

    Long userId = TokenUtil.getJwtInfo().getId();

    //идем в друзья и получаем спискок id друзей и добавляем, для показа новостей друзей, плюс свой id
    if (searchDto.getWithFriends() != null && searchDto.getWithFriends()) {

      List<Long> friendsIds = friendsFeignClient.getFriendId();
      friendsIds.add(userId);
      searchDto.setAccountIds(friendsIds);

      log.info("List of friends ids and currentUserId: {}", friendsIds);
    } else {

      //удаляем из поискового запроса id заблокированных пользователей
      List<Long> blockedFriends = friendsFeignClient.getBlockFriendId();

      if (!blockedFriends.isEmpty())   searchDto.setBlockedIds(blockedFriends);

      log.info("List of blockedFriends: {}", blockedFriends);
    }

    log.info("getAll ends {}", searchDto);

    return postRepository.findAll(getSpecification(searchDto), page).map(post -> {

      // проверяем, ставил ли лайк текущий юзер
      log.info("likeRepository.existsByAuthorIdAndTypeAndItemId begins with userId: {} postId: {}", userId, post.getId());
      Boolean myLike = likeRepository.existsByAuthorIdAndTypeAndItemId(userId, LikeType.POST, post.getId());

      String[] tags =  post.getTagsToPost().stream().map(Tag::getTag).toArray(String[]::new);

      return postMapper.toDto(post, myLike, tags);
    });
  }


  /**
   * Создание нового поста
   * */
  public void create(PostDto dto, Long publishDate) {
    log.info("create begins post {}", dto);
    dto.setAuthorId(TokenUtil.getJwtInfo().getId());
    dto.setPublishDate(secondsToZoned(publishDate));
    dto.setTime(secondsToZoned(publishDate));


    // обрабатываем теги, если теги новые - создаем, если теги уже имеются - закрепляем их за постом.
    Set<Tag> tagSet = proceedTags(dto);

    postRepository.save(postMapper.toEntity(dto)).setTagsToPost(tagSet);

    //получаем список id друзей и отправляем нотификации для всех друзей в microservice-notification о публикации нового поста
    List<Long> friendsIds = friendsFeignClient.getFriendId();

    String content = dto.getTitle().length() > 20 ? dto.getTitle().substring(0, 20) + "..." : dto.getTitle();

    NotificationInputDto notification = new NotificationInputDto(dto.getAuthorId(), null,
        NotificationType.POST, content);

    for (Long friendId : friendsIds){

      notification.setUserId(friendId);

      kafkaSender.sendMessage(topicNotification, "New post notification", notification);
    }

    log.info("create ends");
  }

  /**
   * Метод обработки тегов при создании/редактировании поста
   * */
  public Set<Tag> proceedTags(PostDto dto){
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
    return tagSet;
  }

  /**
   * Редактирование поста
   * */
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
    Set<Tag> tagSet = proceedTags(dto);

    post.setTagsToPost(tagSet);

    //обновляем дату публикации поста
    dto.setTimeChanged(ZonedDateTime.now());

    postMapper.updatePostFromDto(dto, post);
    log.info("update ends");
  }

  /**
   * Удаление поста по id
   * */
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


  /**
   * Метод форматирования времени из секунд в формат ZonedDateTime
   * */
  public ZonedDateTime secondsToZoned(Long seconds){
    if (seconds == null){
      return null;
    } else {
      return ZonedDateTime.ofInstant(Instant.ofEpochSecond(seconds), ZoneId.systemDefault());
    }
  }

  public Specification<Post> getSpecification(PostSearchDto dto) {

    if (dto.getAuthor() != null){
      proceedAuthorSearch(dto);
    }

    return in(Post_.id, dto.getIds(), true)
        .and(containsTag(dto.getTags()))
        .and(between(Post_.time, secondsToZoned(dto.getDateFrom()), secondsToZoned(dto.getDateTo()), true))
        .and(between(Post_.publishDate, ZonedDateTime.now().minusYears(10L), ZonedDateTime.now(), true))
        .and(like(Post_.postText, dto.getPostText(), true))
        .and(like(Post_.title, dto.getTitle(), true))
        .and(equal(Post_.isDelete, dto.getIsDelete(), true))
        .and(exclude(Post_.authorId, dto.getBlockedIds(), true))
        .and(in(Post_.authorId, dto.getAccountIds(), true));

  }

  /**
   * Метод для поиска по авторам, обращается через feign-client в microservice-account и получает id пользователей,
   * для дальнейшего поиска постов по найденным пользователям
   * */
  private void proceedAuthorSearch(PostSearchDto dto) {
    log.info("Search by postAuthor begins");

    String[] authors = dto.getAuthor().split(" ");

    List<Long> accountIds = new ArrayList<>();

    for (String author : authors){
      AccountByFilterDto accountByFilterDto = new AccountByFilterDto();
      accountByFilterDto.setPageNumber(0);
      accountByFilterDto.setPageSize(1000);

      AccountSearchDto searchDto = new AccountSearchDto();
      
      //ищем по имени
      log.info("Searching by firstName: {}", author);
      searchDto.setFirstName(author);
      accountByFilterDto.setAccountSearchDto(searchDto);

      accountIds.addAll(searchAccountWithFeign(accountByFilterDto));

      //ищем по фамилии
      log.info("Searching by lastName: {}", author);
      searchDto.setFirstName(null);
      searchDto.setLastName(author);
      accountByFilterDto.setAccountSearchDto(searchDto);

      accountIds.addAll(searchAccountWithFeign(accountByFilterDto));

    }

    dto.setAccountIds(accountIds);
    log.info("Searching by postAuthor ends, list of accountIds founded: {}", accountIds);
  }

  /**
   * Метод для получения id пользователей по имени/фамилии, обращается через feign-client в microservice-account
   * */
  private List<Long> searchAccountWithFeign(AccountByFilterDto accountByFilterDto) {
    return technicalUserConfig.executeByTechnicalUser(() -> {

      Page<AccountDto> page = accountFeignClient.searchAccountByFilter(accountByFilterDto).getBody();
      if (page != null){
        return page.map(AccountDto::getId).getContent();
      }

      return null;
    });
  }

  public Specification<Post> containsTag(String[] tags){
    return (root, query, builder) -> {
      if (tags == null) return builder.conjunction();
      if (Arrays.toString(tags).equals("[]")) return builder.conjunction();
      log.info("tags: {}", Arrays.toString(tags) );
      Join<Post, Tag> join = root.join(Post_.TAGS_TO_POST);
      return builder.in(join.get(Tag_.TAG)).value(Arrays.asList(tags));
    };
  }



  /**
   * Метод удаления лайка (пересчет количества лайков поста)
   * */
  public void dislike(Long itemId) {
    log.info("dislike begins, postId: {}", itemId);
    Post post = postRepository.findById(itemId)
        .orElseThrow(PostNotFoundException::new);
    post.setLikeAmount(post.getLikeAmount() - 1);
    log.info("dislike ends");
  }

  /**
   * Метод добавления лайка (пересчет количества лайков поста)
   * */
  public void setLike(Long itemId) {
    log.info("setLike begins, postId: {}", itemId);
    Post post = postRepository.findById(itemId)
        .orElseThrow(PostNotFoundException::new);
    post.setLikeAmount(post.getLikeAmount() + 1);
    log.info("setLike ends");
  }
}

