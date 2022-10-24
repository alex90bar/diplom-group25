package ru.skillbox.diplom.group25.microservice.post.resource;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import ru.skillbox.diplom.group25.microservice.account.model.AccountDto;
import ru.skillbox.diplom.group25.microservice.post.dto.PhotoDto;
import ru.skillbox.diplom.group25.microservice.post.dto.PostDto;
import ru.skillbox.diplom.group25.microservice.post.dto.search.PostSearchDto;

/**
 * PostResource
 *
 * @author alex90bar
 */

@RequestMapping("api/v1/post")
public interface PostResource {

  @GetMapping("/{id}")
  ResponseEntity<PostDto> getById(@PathVariable("id") Long id);

  @GetMapping
  ResponseEntity<Page<PostDto>> getAll(PostSearchDto searchDto, Pageable page);

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  void create(@RequestBody PostDto dto, @RequestParam(required = false, value = "publishDate") Long publishDate);

  @PutMapping("/{id}")
  @ResponseStatus(HttpStatus.CREATED)
  void update(@RequestBody PostDto dto, @PathVariable("id") Long id);

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.OK)
  void deleteById(@PathVariable("id") Long id);


  @PostMapping("/storagePostPhoto")
  ResponseEntity<PhotoDto> uploadFile(@RequestParam MultipartFile file);




}
