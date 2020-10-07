package com.codeoftheweb.salvo.dao;


import java.util.Optional;

import com.codeoftheweb.salvo.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findById(long id);
}
