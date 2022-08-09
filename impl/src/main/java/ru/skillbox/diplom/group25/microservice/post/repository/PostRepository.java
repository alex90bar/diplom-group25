package ru.skillbox.diplom.group25.microservice.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ru.skillbox.diplom.group25.microservice.post.model.Post;

/**
 * PostRepository
 *
 * @author alex90bar
 */

public interface PostRepository
    extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

}
