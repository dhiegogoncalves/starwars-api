package com.project.starwars.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import com.project.starwars.dtos.CreatePlanetDTO;
import com.project.starwars.dtos.PlanetDTO;
import com.project.starwars.dtos.PlanetFilterDTO;
import com.project.starwars.dtos.UpdatePlanetDTO;
import com.project.starwars.models.Planet;
import com.project.starwars.services.PlanetService;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/planets")
@RequiredArgsConstructor
@Api(tags = { "Planet API v1" })
public class PlanetController {

    private final PlanetService planetService;
    private final ModelMapper modelMapper;

    @GetMapping
    @ApiOperation("Find planets by params")
    public Page<PlanetDTO> findAll(PlanetFilterDTO planetFilterDTO, Pageable pageRequest) {
        Planet filter = modelMapper.map(planetFilterDTO, Planet.class);
        Page<Planet> result = planetService.findAll(filter, pageRequest);
        List<PlanetDTO> list = result.getContent().stream().map(entity -> modelMapper.map(entity, PlanetDTO.class))
                .collect(Collectors.toList());

        return new PageImpl<>(list, pageRequest, result.getTotalElements());
    }

    @GetMapping("{id}")
    @ApiOperation("Get a planet by id")
    public Planet findById(@PathVariable String id) {
        return planetService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("name/{name}")
    @ApiOperation("Get a planet by name")
    public Planet findByName(@PathVariable String name) {
        return planetService.findByName(name).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Create a planet")
    public PlanetDTO create(@Valid @RequestBody CreatePlanetDTO createPlanetDTO) {
        Planet planet = modelMapper.map(createPlanetDTO, Planet.class);
        planet = planetService.save(planet);
        return modelMapper.map(planet, PlanetDTO.class);
    }

    @PutMapping("{id}")
    @ApiOperation("Update a planet")
    public PlanetDTO update(@PathVariable String id, @Valid @RequestBody UpdatePlanetDTO updatePlanetDTO) {
        return planetService.findById(id).map(planet -> {
            planet.setClimate(updatePlanetDTO.getClimate());
            planet.setTerrain(updatePlanetDTO.getTerrain());
            planet = planetService.update(planet);
            return modelMapper.map(planet, PlanetDTO.class);
        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Delete a planet by id")
    public void delete(@PathVariable String id) {
        Planet planet = planetService.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        planetService.delete(planet);
    }
}
