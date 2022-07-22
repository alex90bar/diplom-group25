package ru.skillbox.diplom.group25.microservice.post.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.logging.Logger;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.skillbox.diplom.group25.microservice.post.model.Post;
import ru.skillbox.diplom.group25.microservice.post.repository.PostRepository;
import ru.skillbox.diplom.group25.microservice.post.request.PostAddRq;
import ru.skillbox.diplom.group25.microservice.post.response.PostRs;

/**
 * PostService
 *
 * @author alex90bar
 */

@Service
@RequiredArgsConstructor
@Setter
public class PostService {

  private final PostRepository postRepository;

  public void addNewPost(String publishDate, PostAddRq postAddRq){ //TODO лучше убрать суфикс POST например create() дальше по аналогии
    Logger.getLogger(this.getClass().getCanonicalName()).info("addNewPost begins");

    //TODO Mapstruct PostRequest to PostEntity;

    Post post = new Post(); //TODO такое не буду пропускать)) делайте через mapstruct
    post.setAuthorId(1);
    post.setPostText(postAddRq.getPostText());
    post.setTitle(postAddRq.getTitle());
    post.setImagePath(postAddRq.getPhotoUrl());
    post.setIsBlocked(false);
    post.setIsDelete(false);
    post.setLikeAmount(0);
    post.setTime(System.currentTimeMillis());
    post.setMyLike(false);

    postRepository.save(post);

    Logger.getLogger(this.getClass().getCanonicalName()).info("addNewPost ends");
  }

  @Transactional(readOnly = true)  //TODO лучше вешать над классом, а здесь переопределять в методе
  public PostRs getPostById(String id){
    Logger.getLogger(this.getClass().getCanonicalName()).info("getPostById begins");

    Integer postId = Integer.parseInt(id);
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException(id.toString()));

    //TODO MapStruct Post to PostRs

    PostRs postRs = new PostRs();
    postRs.setData(post.getPostText());

    Logger.getLogger(this.getClass().getCanonicalName()).info("getPostById ends");

    return postRs;
  }

  public PostRs deletePostById(String id) {
    Logger.getLogger(this.getClass().getCanonicalName()).info("deletePostById begins");

    Integer postId = Integer.parseInt(id);
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException(id.toString()));

    postRepository.delete(post);

    //TODO MapStruct Post to PostRs

    Logger.getLogger(this.getClass().getCanonicalName()).info("deletePostById ends");

    return new PostRs();
  }
}


