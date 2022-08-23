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
  @Mapping(target = "authorId", source = "author")
  @Mapping(target = "isDelete", constant = "false")
  @Mapping(target = "likeAmount", source = "likes")
  @Mapping(target = "imagePath", source = "photoUrl")
  @Mapping(target = "time", expression = "java(newTime())")
  Post toEntity(PostDto dto);

  @Mapping(target = "author", source = "authorId")
  @Mapping(target = "type", constant = "POSTED")
  @Mapping(target = "comments", expression = "java(newPostCommentDto())")
  @Mapping(target = "tags", expression = "java(newPostTagDto())")
  @Mapping(target = "likes", source = "likeAmount")
  @Mapping(target = "photoUrl", source = "imagePath")
  PostDto toDto(Post entity);

  @Mapping(target = "authorId", source = "author")
  @Mapping(target = "likeAmount", source = "likes")
  @Mapping(target = "imagePath", source = "photoUrl")
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


