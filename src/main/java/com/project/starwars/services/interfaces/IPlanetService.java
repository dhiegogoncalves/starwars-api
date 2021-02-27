package com.project.starwars.services.interfaces;

import java.util.Optional;

import com.project.starwars.models.Planet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPlanetService {

    Page<Planet> findAll(Planet filter, Pageable pageRequest);

    Optional<Planet> findById(String id);

    Optional<Planet> findByName(String name);

    Planet save(Planet planet);

    Planet update(Planet planet);

    void delete(Planet planet);
}
