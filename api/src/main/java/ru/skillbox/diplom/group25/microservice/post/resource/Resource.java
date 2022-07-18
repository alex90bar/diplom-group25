package ru.skillbox.diplom.group25.microservice.post.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.skillbox.diplom.group25.microservice.post.dto.Dto;

/**
 * Resource
 *
 * @author alex90bar
 */

@RequestMapping("api/v1/person/")
public interface Resource {

  @GetMapping(value = "/{id}}")
  Dto getById(@PathVariable(name = "id") Long id);

}
