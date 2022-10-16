package ru.skillbox.diplom.group25.microservice.post.model;

import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Tag
 *
 * @author alex90bar
 */

@Getter
@Setter
@Entity
@Table(name = "tag")
@RequiredArgsConstructor
public class Tag {

  @Id
  @Column(name = "id", nullable = false)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "tag", nullable = false)
  private String tag;

  @ManyToMany(mappedBy = "tagsToPost")
  private Set<Post> postsOfTag;

}


