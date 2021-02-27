package com.project.starwars.services;

import java.util.Optional;

import com.project.starwars.dtos.api.ApiPlanetDTO;
import com.project.starwars.exceptions.BusinessException;
import com.project.starwars.models.Planet;
import com.project.starwars.repositories.PlanetRepository;
import com.project.starwars.repositories.api.SwApiRepository;
import com.project.starwars.services.interfaces.IPlanetService;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PlanetService implements IPlanetService {

    private final PlanetRepository planetRepository;
    private final SwApiRepository swApiRepository;

    public Page<Planet> findAll(Planet filter, Pageable pageRequest) {
        Example<Planet> example = Example.of(filter, ExampleMatcher.matching().withIgnoreCase().withIgnoreNullValues()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));
        return planetRepository.findAll(example, pageRequest);
    }

    public Optional<Planet> findById(String id) {
        return planetRepository.findById(id);
    }

    public Optional<Planet> findByName(String name) {
        return planetRepository.findByNameIgnoreCase(name);
    }

    public Planet save(Planet planet) {
        if (planetRepository.existsByNameIgnoreCase(planet.getName())) {
            throw new BusinessException("Planeta já cadastrado");
        }

        ApiPlanetDTO apiPlanetDTO = swApiRepository.findPlanetByName(planet.getName());
        if (apiPlanetDTO == null) {
            throw new BusinessException(
                    "Este planeta não existe no universo do Star Wars, por favor tente cadastrar outro planeta");
        }

        planet.setFilmAppearances(apiPlanetDTO.getFilms().size());
        planet.setFilms(apiPlanetDTO.getFilms());

        return planetRepository.save(planet);
    }

    public Planet update(Planet planet) {
        return planetRepository.save(planet);
    }

    public void delete(Planet planet) {
        planetRepository.delete(planet);
    }

}
