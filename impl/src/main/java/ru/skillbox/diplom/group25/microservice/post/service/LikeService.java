package ru.skillbox.diplom.group25.microservice.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group25.library.core.util.TokenUtil;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeDto;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeType;
import ru.skillbox.diplom.group25.microservice.post.mapper.LikeMapper;
import ru.skillbox.diplom.group25.microservice.post.repository.LikeRepository;

/**
 * LikeService
 *
 * @author alex90bar
 */

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {

  private final LikeRepository likeRepository;
  private final PostService postService;
  private final CommentService commentService;
  private final LikeMapper mapper;

//    Проверяем, есть ли лайк в БД, если нет - то создаем.
//    Пересчитываем количество лайков в соответствующем посте/коменте.
  public void create(LikeDto dto) {
    log.info("create begins " + dto);

    Long userId = TokenUtil.getJwtInfo().getId();
    dto.setAuthorId(userId);

    if (!likeRepository.existsByAuthorIdAndTypeAndItemId(userId, dto.getType(), dto.getItem_id())){
      likeRepository.save(mapper.toEntity(dto));
      if (dto.getType().equals(LikeType.POST)){
        postService.setLike(dto.getItem_id());
      } else {
        commentService.setLike(dto.getItem_id());
      }
    } else {
      log.info("Like already exists with: " + dto);
    }
    log.info("create ends");
  }

  //Проверяем наличие лайка в БД, если есть - удаляем.
  //Пересчитываем количество лайков в соответствующем посте/коменте.
  public void delete(LikeDto dto){
    log.info("delete begins " + dto);

    Long userId = TokenUtil.getJwtInfo().getId();
    if (likeRepository.existsByAuthorIdAndTypeAndItemId(userId, dto.getType(), dto.getItem_id())) {
      likeRepository.deleteByAuthorIdAndTypeAndItemId(userId, dto.getType(), dto.getItem_id());
      if (dto.getType().equals(LikeType.POST)) {
        postService.dislike(dto.getItem_id());
      } else {
        commentService.dislike(dto.getItem_id());
      }
    } else {
      log.info("Like not found with: " + dto);
    }
    log.info("delete ends ");
  }

}


