package ru.skillbox.diplom.group25.microservice.post.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group25.microservice.post.model.Comment;
import ru.skillbox.diplom.group25.microservice.post.model.Post;

/**
 * CommentRepository
 *
 * @author alex90bar
 */

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, JpaSpecificationExecutor<Comment> {

  List<Comment> findAllByPostIdOrderByIdAsc(Long postId);
}
