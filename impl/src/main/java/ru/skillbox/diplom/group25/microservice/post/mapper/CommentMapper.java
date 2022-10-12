package ru.skillbox.diplom.group25.microservice.post.mapper;

import java.time.ZonedDateTime;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentDto;
import ru.skillbox.diplom.group25.microservice.post.dto.CommentType;
import ru.skillbox.diplom.group25.microservice.post.model.Comment;

/**
 * CommentMapper
 *
 * @author alex90bar
 */

@Mapper(componentModel = "spring")
public interface CommentMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "time", expression = "java(newTime())")
  @Mapping(target = "parentId", defaultValue = "0L")
  @Mapping(target = "likeAmount", defaultValue = "0")
  @Mapping(target = "imagePath", defaultValue = "http://dummyimage.com/130x60/a6a6ff")
  @Mapping(target = "post.id", source = "postId")
  @Mapping(target = "commentsCount", defaultValue = "0")
  Comment toEntity(CommentDto dto);


  @Mapping(target = "commentType", expression = "java(getCommentType(entity.getParentId()))")
  @Mapping(target = "postId", source = "entity.post.id")
  @Mapping(target = "myLike", source = "myLike")
  @Mapping(target = "isBlocked", defaultValue = "false")
  @Mapping(target = "isDelete", defaultValue = "false")
  CommentDto toDto(Comment entity, Boolean myLike);

  @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
  void updateCommentFromDto(CommentDto dto, @MappingTarget Comment comment);

  default ZonedDateTime newTime() {
    return ZonedDateTime.now();
  }

  default CommentType getCommentType(Long parentId){
    return parentId == 0L ? CommentType.POST : CommentType.COMMENT;
  }

}
