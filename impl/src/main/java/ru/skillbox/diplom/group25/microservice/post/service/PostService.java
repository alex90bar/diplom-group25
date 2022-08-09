package ru.skillbox.diplom.group25.microservice.post.service;

import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.equal;
import static ru.skillbox.diplom.group25.library.core.repository.SpecificationUtils.in;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.webjars.NotFoundException;
import ru.skillbox.diplom.group25.microservice.account.model.AccountDto;
import ru.skillbox.diplom.group25.microservice.post.client.AccountServiceFeignClient;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.request.PostAddRq;
import ru.skillbox.diplom.group25.microservice.post.dto.request.PostSearchDto;
import ru.skillbox.diplom.group25.microservice.post.dto.response.PostRs;
import ru.skillbox.diplom.group25.microservice.post.mapper.PostMapper;
import ru.skillbox.diplom.group25.microservice.post.model.Post;
import ru.skillbox.diplom.group25.microservice.post.model.Post_;
import ru.skillbox.diplom.group25.microservice.post.repository.PostRepository;

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
  private final PostMapper postMapper;
  private final AccountServiceFeignClient accountServiceFeignClient;

  public void create(String publishDate, PostAddRq postAddRq) {
    log.info("create begins");

    if (publishDate == null) {
      publishDate = "-1";
    }

    Post post = postMapper.toPostFromPostAddRq(postAddRq);

    postRepository.save(post);

    log.info("create ends");
  }

  @Transactional(readOnly = true)
  public PostRs getById(String id) {
    log.info("getById begins");

    Long postId = Long.valueOf(id);
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

    //получаем accountDto из account-service по id через Feign
    AccountDto accountDto = accountServiceFeignClient.getAccountById(post.getAuthorId()); //TODO использовать из аккаунта сервиса

    //заворачиваем сущность в дто и вставляем accountDto
    PostDto postDto = postMapper.toPostDtoFromPost(post);
    postDto.setAuthor(accountDto);
    PostRs postRs = postMapper.toPostRsFromPostDto(postDto);

    log.info("getById ends");

    return postRs;
  }

  public PostRs deleteById(String id) {
    log.info("deleteById begins");

    Long postId = Long.valueOf(id);
    Post post = postRepository.findById(postId)
        .orElseThrow(() -> new NotFoundException("Post not found with id: " + id));

    postRepository.delete(post);

    log.info("deleteById ends");

    return new PostRs();
  }

  public List<PostDto> searchByDto(PostSearchDto dto) {
    log.info("searchByDto begins: " + dto.toString());
    return postRepository.findAll(getSpecification(dto))
        .stream()
        .map(postMapper::toPostDtoFromPost)
        .collect(Collectors.toList());
  }

  public Specification<Post> getSpecification(PostSearchDto dto) {
    return in(Post_.id, dto.getIds(), true)
            .and(equal(Post_.postText, dto.getPostText(), true))
            .and(equal(Post_.title, dto.getTitle(), true))
            .and(in(Post_.authorId, dto.getAccountIds(), true));
  }
}


