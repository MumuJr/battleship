package com.codeoftheweb.salvo.dao;

import java.util.List;

import com.codeoftheweb.salvo.model.Ship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface ShipRepository extends JpaRepository<Ship, Long> {
        List<Ship> findById(long id);
    }
