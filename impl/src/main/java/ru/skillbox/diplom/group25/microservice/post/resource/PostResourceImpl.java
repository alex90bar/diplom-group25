package ru.skillbox.diplom.group25.microservice.post.resource;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group25.microservice.post.dto.PhotoDto;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.search.PostSearchDto;
import ru.skillbox.diplom.group25.microservice.post.service.PostService;
import ru.skillbox.diplom.group25.microservice.post.service.StorageService;

/**
 * PostResourceImpl
 *
 * @author alex90bar
 */

@RestController
@RequiredArgsConstructor
public class PostResourceImpl implements PostResource {

  private final PostService postService;
  private final StorageService storageService;

  @Override
  public ResponseEntity<PostDto> getById(Long id) {
    return ResponseEntity.ok(postService.getById(id));
  }

  @Override
  public ResponseEntity<Page<PostDto>> getAll(PostSearchDto searchDto, Pageable page) {
    return ResponseEntity.ok(postService.getAll(searchDto, page));
  }

  @Override
  public void create(PostDto dto) {
    postService.create(dto);
  }

  @Override
  public void update(PostDto dto, Long id) {
    postService.update(dto, id);
  }

  @Override
  public void deleteById(Long id) {
    postService.deleteById(id);
  }

  @Override
  public ResponseEntity<PhotoDto> uploadFile(MultipartFile file) {
    return storageService.uploadPhoto(file);
  }

}


