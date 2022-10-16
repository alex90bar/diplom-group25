package ru.skillbox.diplom.group25.microservice.post.mapper;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.PostTagDto;
import ru.skillbox.diplom.group25.microservice.post.model.Post;

/**
 * PostMapper
 *
 * @author alex90bar
 */

@Mapper(componentModel = "spring")
public interface PostMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "imagePath", defaultValue = "")
  @Mapping(target = "time", expression = "java(newTime())")
  @Mapping(target = "publishDate", expression = "java(newTime())")
  @Mapping(target = "likeAmount", defaultValue = "0")
  @Mapping(target = "commentsCount", defaultValue = "0")
  @Mapping(target = "myLike", defaultValue = "false")
  @Mapping(target = "isBlocked", defaultValue = "false")
  @Mapping(target = "isDelete", defaultValue = "false")
  Post toEntity(PostDto dto);

  @Mapping(target = "type", constant = "POSTED")
  @Mapping(target = "myLike", source = "myLike")
  @Mapping(target = "tags", source = "tags")
  PostDto toDto(Post entity, Boolean myLike, String[] tags);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updatePostFromDto(PostDto dto, @MappingTarget Post post);

  default List<PostTagDto> newPostTagDto(){
    return new ArrayList<>();
  }

  default List<CommentDto> newPostCommentDto(){
    return new ArrayList<>();
  }

  default ZonedDateTime newTime() {
    return ZonedDateTime.now();
  }

}


