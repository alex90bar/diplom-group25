package ru.skillbox.diplom.group25.microservice.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
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



//    Проверяем, есть ли лайк в БД, если есть - то удаляем, если нет - то создаем.
//    Пересчитываем количество лайков в соответствующем посте/коменте.
  public void create(LikeDto dto) {
    log.info("create begins " + dto);
    if (likeRepository.existsByAuthorIdAndTypeAndItemId(dto.getAuthorId(), dto.getType(), dto.getItemId())){
      likeRepository.deleteByAuthorIdAndTypeAndItemId(dto.getAuthorId(), dto.getType(), dto.getItemId());
      if (dto.getType().equals(LikeType.POST)){
        postService.dislike(dto.getItemId());
      } else {
        commentService.dislike(dto.getItemId());
      }
    } else {
      likeRepository.save(mapper.toEntity(dto));
      if (dto.getType().equals(LikeType.POST)){
        postService.setLike(dto.getItemId());
      } else {
        commentService.setLike(dto.getItemId());
      }
    }
    log.info("create ends");
  }
}


