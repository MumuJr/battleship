package com.codeoftheweb.salvo.dao;
import com.codeoftheweb.salvo.model.ForComments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import java.util.List;

@RepositoryRestResource
public interface ForCommentsRepository extends JpaRepository<ForComments, Long> {
    List<ForComments> findById(long id);
}