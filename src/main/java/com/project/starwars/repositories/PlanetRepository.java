package com.project.starwars.repositories;

import java.util.Optional;

import com.project.starwars.models.Planet;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlanetRepository extends MongoRepository<Planet, String> {
    Optional<Planet> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}
