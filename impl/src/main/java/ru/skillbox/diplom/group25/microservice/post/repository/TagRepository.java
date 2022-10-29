package ru.skillbox.diplom.group25.microservice.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.skillbox.diplom.group25.microservice.post.model.Tag;

/**
 * TagRepository
 *
 * @author alex90bar
 */

@Repository
public interface TagRepository extends JpaRepository<Tag, Long>, JpaSpecificationExecutor<Tag> {

  Tag findByTag(String tag);

}
