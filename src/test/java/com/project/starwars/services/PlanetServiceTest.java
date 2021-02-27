package com.project.starwars.services;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.project.starwars.dtos.api.ApiPlanetDTO;
import com.project.starwars.exceptions.BusinessException;
import com.project.starwars.models.Planet;
import com.project.starwars.repositories.PlanetRepository;
import com.project.starwars.repositories.api.SwApiRepository;
import com.project.starwars.services.interfaces.IPlanetService;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class PlanetServiceTest {

    IPlanetService planetService;

    @MockBean
    PlanetRepository planetRepository;

    @MockBean
    SwApiRepository swApiRepository;

    @BeforeEach
    void setUp() {
        this.planetService = new PlanetService(planetRepository, swApiRepository);
    }

    @Test
    @DisplayName("Deve filtrar planetas pelas propriedades")
    void DeveFiltrarPlanetasPelasPropriedadesTest() {
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        List<Planet> planetList = Arrays.asList(planet);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<Planet> page = new PageImpl<Planet>(planetList, pageRequest, 1);

        Mockito.when(planetRepository.findAll(Mockito.any(Example.class), Mockito.any(PageRequest.class)))
                .thenReturn(page);

        Page<Planet> result = planetService.findAll(planet, pageRequest);

        Assertions.assertThat(result.getTotalElements()).isEqualTo(1);
        Assertions.assertThat(result.getContent()).isEqualTo(planetList);
        Assertions.assertThat(result.getPageable().getPageNumber()).isEqualTo(0);
        Assertions.assertThat(result.getPageable().getPageSize()).isEqualTo(10);
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

        Mockito.when(planetRepository.findById(id)).thenReturn(Optional.of(planet));

        Optional<Planet> foundPlanet = planetService.findById(id);

        Assertions.assertThat(foundPlanet.isPresent()).isTrue();
        Assertions.assertThat(foundPlanet.get().getId()).isEqualTo(planet.getId());
        Assertions.assertThat(foundPlanet.get().getName()).isEqualTo(planet.getName());
        Assertions.assertThat(foundPlanet.get().getClimate()).isEqualTo(planet.getClimate());
        Assertions.assertThat(foundPlanet.get().getTerrain()).isEqualTo(planet.getTerrain());
        Assertions.assertThat(foundPlanet.get().getFilmAppearances()).isEqualTo(planet.getFilmAppearances());
        Assertions.assertThat(foundPlanet.get().getFilms()).isEqualTo(planet.getFilms());
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

        Mockito.when(planetRepository.findByNameIgnoreCase(name)).thenReturn(Optional.of(planet));

        Optional<Planet> foundPlanet = planetService.findByName(name);

        Assertions.assertThat(foundPlanet.isPresent()).isTrue();
        Assertions.assertThat(foundPlanet.get().getId()).isEqualTo(planet.getId());
        Assertions.assertThat(foundPlanet.get().getName()).isEqualTo(planet.getName());
        Assertions.assertThat(foundPlanet.get().getClimate()).isEqualTo(planet.getClimate());
        Assertions.assertThat(foundPlanet.get().getTerrain()).isEqualTo(planet.getTerrain());
        Assertions.assertThat(foundPlanet.get().getFilmAppearances()).isEqualTo(planet.getFilmAppearances());
        Assertions.assertThat(foundPlanet.get().getFilms()).isEqualTo(planet.getFilms());
    }

    @Test
    @DisplayName("Deve retornar vazio quando obter um planeta por Id inexistente")
    void DeveRetornarVazioQuandoObterPlanetaPorIdInexistenteTest() {
        String id = "60390340bd296130055aaa35";
        Mockito.when(planetRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Planet> planet = planetService.findById(id);

        Assertions.assertThat(planet.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve retornar vazio quando obter um planeta por Name inexistente")
    void DeveRetornarVazioQuandoObterPlanetaPorNameInexistenteTest() {
        String name = "tatooine";
        Mockito.when(planetRepository.findByNameIgnoreCase(name)).thenReturn(Optional.empty());

        Optional<Planet> planet = planetService.findByName(name);

        Assertions.assertThat(planet.isPresent()).isFalse();
    }

    @Test
    @DisplayName("Deve lançar Exception quando tentar salvar um planeta existente")
    void DeveLancarExceptionQuandoTentarSalvarPlanetaExistenteTest() {
        Planet planet = Planet.builder().name("Tatooine").climate("arid").terrain("desert").build();

        Mockito.when(planetRepository.existsByNameIgnoreCase(planet.getName())).thenReturn(true);

        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> planetService.save(planet));

        Mockito.verify(planetRepository, Mockito.never()).save(planet);

        Assertions.assertThatExceptionOfType(BusinessException.class).isThrownBy(() -> {
            planetService.save(planet);
        }).withMessage("Planeta já cadastrado");
    }

    @Test
    @DisplayName("Deve lançar Exception quando tentar salvar um planeta inexistente na consulta da Api")
    void DeveLancarExceptionQuandoTentarSalvarPlanetaInexistenteNaConsultaDaApiTest() {
        Planet planet = Planet.builder().name("Tatooine").climate("arid").terrain("desert").build();

        Mockito.when(planetRepository.existsByNameIgnoreCase(planet.getName())).thenReturn(false);

        Mockito.when(swApiRepository.findPlanetByName(planet.getName())).thenReturn(null);

        org.junit.jupiter.api.Assertions.assertThrows(BusinessException.class, () -> planetService.save(planet));

        Mockito.verify(planetRepository, Mockito.never()).save(planet);

        Assertions.assertThatExceptionOfType(BusinessException.class).isThrownBy(() -> {
            planetService.save(planet);
        }).withMessage("Este planeta não existe no universo do Star Wars, por favor tente cadastrar outro planeta");
    }

    @Test
    @DisplayName("Deve salvar um planeta")
    void DeveSalvarPlanetaTest() {
        Planet planet = Planet.builder().name("Tatooine").climate("arid").terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        Planet result = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        ApiPlanetDTO apiPlanetDTO = ApiPlanetDTO.builder().name("Tatooine").climate("arid").terrain("desert")
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        Mockito.when(planetRepository.existsByNameIgnoreCase(planet.getName())).thenReturn(false);
        Mockito.when(swApiRepository.findPlanetByName(planet.getName())).thenReturn(apiPlanetDTO);
        Mockito.when(planetService.save(planet)).thenReturn(result);

        Planet savedPlanet = planetService.save(planet);

        Assertions.assertThat(savedPlanet.getId()).isNotNull();
        Assertions.assertThat(savedPlanet.getName()).isEqualTo(planet.getName());
        Assertions.assertThat(savedPlanet.getClimate()).isEqualTo(planet.getClimate());
        Assertions.assertThat(savedPlanet.getTerrain()).isEqualTo(planet.getTerrain());
        Assertions.assertThat(savedPlanet.getFilmAppearances()).isEqualTo(planet.getFilmAppearances());
        Assertions.assertThat(savedPlanet.getFilms()).isEqualTo(planet.getFilms());
    }

    @Test
    @DisplayName("Deve atualizar um planeta")
    void DeveAtualizarPlanetaTest() {
        Planet updatingPlanet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        Planet updatedPlanet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("temperate")
                .terrain("grasslands, mountains").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        Mockito.when(planetRepository.save(updatingPlanet)).thenReturn(updatedPlanet);

        Planet planet = planetService.update(updatingPlanet);

        Assertions.assertThat(planet.getId()).isEqualTo(updatedPlanet.getId());
        Assertions.assertThat(planet.getClimate()).isEqualTo(updatedPlanet.getClimate());
        Assertions.assertThat(planet.getTerrain()).isEqualTo(updatedPlanet.getTerrain());
        Assertions.assertThat(planet.getFilmAppearances()).isEqualTo(updatedPlanet.getFilmAppearances());
        Assertions.assertThat(planet.getFilms()).isEqualTo(updatedPlanet.getFilms());
    }

    @Test
    @DisplayName("Deve deletar um planeta")
    void DeveDeletarPlanetaTest() {
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        org.junit.jupiter.api.Assertions.assertDoesNotThrow(() -> planetService.delete(planet));

        Mockito.verify(planetRepository, Mockito.times(1)).delete(planet);
    }
}
