package com.project.starwars.repositories;

import java.util.Arrays;
import java.util.Optional;

import com.project.starwars.models.Planet;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataMongoTest
public class PlanetRepositoryTest {

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    PlanetRepository planetRepository;

    @AfterEach
    void clean() {
        mongoTemplate.dropCollection("planet");
    }

    @Test
    @DisplayName("Deve retornar Verdadeiro quando existir um planeta na base com o Name informado")
    void DeveRetornarVerdadeiroQuandoExistirPlanetaNaBaseComNameInformadoTest() {
        String name = "tatooine";
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();
        mongoTemplate.save(planet, "planet");

        boolean exists = planetRepository.existsByNameIgnoreCase(name);

        Assertions.assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar Falso quando não existir um planeta na base com o Name informado")
    void DeveRetornarFalsoQuandoNaoExistirPlanetaNaBaseComNameInformadoTest() {
        String name = "tatooine";

        boolean exists = planetRepository.existsByNameIgnoreCase(name);

        Assertions.assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve obter um planeta por Id")
    void DeveObterPlanetaPorIdTest() {
        String id = "60390340bd296130055aaa35";
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();
        mongoTemplate.save(planet, "planet");

        Optional<Planet> foundPlanet = planetRepository.findById(id);

        Assertions.assertThat(foundPlanet.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve obter um planeta por Name")
    void DeveObterPlanetaPorNameTest() {
        String name = "tatooine";
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();
        mongoTemplate.save(planet, "planet");

        Optional<Planet> foundPlanet = planetRepository.findByNameIgnoreCase(name);

        Assertions.assertThat(foundPlanet.isPresent()).isTrue();
    }

    @Test
    @DisplayName("Deve salvar um planeta")
    void DeveSalvarPlanetaTest() {
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        Planet savedPlanet = planetRepository.save(planet);

        Assertions.assertThat(savedPlanet.getId()).isNotNull();
    }

    @Test
    @DisplayName("Deve atualizar um planeta")
    void DeveAtualizarPlanetaTest() {
        String id = "60390340bd296130055aaa35";
        String climate = "temperate";
        String terrain = "grasslands, mountains";
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();
        mongoTemplate.save(planet, "planet");

        Optional<Planet> foundPlanet = planetRepository.findById(id);

        foundPlanet.get().setClimate(climate);
        foundPlanet.get().setTerrain(terrain);

        Planet updatedPlanet = planetRepository.save(foundPlanet.get());

        Assertions.assertThat(updatedPlanet.getClimate()).isEqualTo(climate);
        Assertions.assertThat(updatedPlanet.getTerrain()).isEqualTo(terrain);
    }

    @Test
    @DisplayName("Deve deletar um planeta")
    void DeveDeletarPlanetaTest() {
        String id = "60390340bd296130055aaa35";
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();
        mongoTemplate.save(planet, "planet");

        Optional<Planet> foundPlanet = planetRepository.findById(id);
        planetRepository.delete(foundPlanet.get());
        Optional<Planet> deletedPlanet = planetRepository.findById(id);

        Assertions.assertThat(deletedPlanet.isEmpty()).isTrue();
    }
}