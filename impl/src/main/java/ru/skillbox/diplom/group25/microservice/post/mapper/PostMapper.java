package ru.skillbox.diplom.group25.microservice.post.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.skillbox.diplom.group25.microservice.account.model.AccountDto;
import ru.skillbox.diplom.group25.microservice.post.dto.PostCommentDto;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.PostTagDto;
import ru.skillbox.diplom.group25.microservice.post.dto.request.PostAddRq;
import ru.skillbox.diplom.group25.microservice.post.dto.response.PostRs;
import ru.skillbox.diplom.group25.microservice.post.model.Post;

/**
 * PostMapper
 *
 * @author alex90bar
 */

@Mapper(componentModel = "spring")
public interface PostMapper {

  @Mapping(target = "id", ignore = true)
  @Mapping(target = "postText")
  @Mapping(target = "title")
  @Mapping(target = "imagePath", source = "photoUrl")
  @Mapping(target = "isBlocked", constant = "false")
  @Mapping(target = "isDelete", constant = "false")
  @Mapping(target = "likeAmount", constant = "0")
  @Mapping(target = "authorId", constant = "1L")
  @Mapping(target = "time", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "myLike", constant = "false")
  Post toPostFromPostAddRq(PostAddRq dto);

  @Mapping(target = "timestamp", expression = "java(System.currentTimeMillis())")
  @Mapping(target = "page", constant = "1")
  @Mapping(target = "size", constant = "20")
  @Mapping(target = "total", constant = "1")
  @Mapping(target = "data", source = "postDto")
  PostRs toPostRsFromPostDto(PostDto postDto);


  @Mapping(target = "author", expression = "java(newAccountDto())")
  @Mapping(target = "type", constant = "POSTED")
  @Mapping(target = "comments", expression = "java(newPostCommentDto())")
  @Mapping(target = "tags", expression = "java(newPostTagDto())")
  @Mapping(target = "likes", source = "likeAmount")
  @Mapping(target = "photoUrl", source = "imagePath")
  PostDto toPostDtoFromPost(Post post);

  default PostTagDto newPostTagDto(){
    return new PostTagDto();
  }

  default AccountDto newAccountDto(){
    return new AccountDto();
  }

  default PostCommentDto newPostCommentDto(){
    return new PostCommentDto();
  }

}


