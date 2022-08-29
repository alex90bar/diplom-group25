package ru.skillbox.diplom.group25.microservice.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group25.microservice.post.dto.LikeType;
import ru.skillbox.diplom.group25.microservice.post.model.Like;

/**
 * LikeRepository
 *
 * @author alex90bar
 */

@Repository
public interface LikeRepository extends JpaRepository<Like, Long>, JpaSpecificationExecutor<Like> {

  boolean existsByAuthorIdAndTypeAndItemId(Long authorId, LikeType type, Long itemId);

  void deleteByAuthorIdAndTypeAndItemId(Long authorId, LikeType type, Long itemId);

}
