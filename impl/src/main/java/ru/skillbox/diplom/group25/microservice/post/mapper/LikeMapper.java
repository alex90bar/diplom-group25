package ru.skillbox.diplom.group25.microservice.post.mapper;

import java.time.ZonedDateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeDto;
import ru.skillbox.diplom.group25.microservice.post.model.Like;

/**
 * LikeMapper
 *
 * @author alex90bar
 */

@Mapper(componentModel = "spring")
public interface LikeMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "time", expression = "java(newTime())")
  Like toEntity (LikeDto dto);

  default ZonedDateTime newTime() {
    return ZonedDateTime.now();
  }

}
