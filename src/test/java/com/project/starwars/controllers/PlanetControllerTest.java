package com.project.starwars.controllers;

import java.util.Arrays;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.starwars.dtos.CreatePlanetDTO;
import com.project.starwars.dtos.UpdatePlanetDTO;
import com.project.starwars.exceptions.BusinessException;
import com.project.starwars.models.Planet;
import com.project.starwars.services.PlanetService;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest(controllers = PlanetController.class)
@AutoConfigureMockMvc
public class PlanetControllerTest {

    static String PLANET_API_URL = "/api/v1/planets";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PlanetService planetService;

    @Test
    @DisplayName("Deve filtrar planetas pelas propriedades")
    void DeveFiltrarPlanetasPelasPropriedadesTest() throws Exception {
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        BDDMockito.given(planetService.findAll(Mockito.any(Planet.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<Planet>(Arrays.asList(planet), PageRequest.of(0, 10), 1));

        String queryString = String.format("?name=%s&author=%s&page=0&size=10", planet.getName(), planet.getClimate());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(PLANET_API_URL.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("content", Matchers.hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("totalElements").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageSize").value(10))
                .andExpect(MockMvcResultMatchers.jsonPath("pageable.pageNumber").value(0));
    }

    @Test
    @DisplayName("Deve retornar Not Found quando procurar um planeta por Id inexistente")
    void DeveRetornarNotFoundQuandoProcurarPlanetaPorIdInexistenteTest() throws Exception {
        String id = "60390340bd296130055aaa35";
        BDDMockito.given(planetService.findById(id)).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(PLANET_API_URL.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar Not Found quando procurar um planeta por Name inexistente")
    void DeveRetornarNotFoundQuandoProcurarPlanetaPorNameInexistenteTest() throws Exception {
        String name = "Tatooine";
        BDDMockito.given(planetService.findByName(name)).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(PLANET_API_URL.concat("/name/" + name))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve retornar um planeta quando procurado por Id existente")
    void DeveRetornarPlanetaQuandoProcuradoPorIdExistenteTest() throws Exception {
        String id = "60390340bd296130055aaa35";
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        BDDMockito.given(planetService.findById(id)).willReturn(Optional.of(planet));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(PLANET_API_URL.concat("/" + id))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(planet.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(planet.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("climate").value(planet.getClimate()))
                .andExpect(MockMvcResultMatchers.jsonPath("terrain").value(planet.getTerrain()))
                .andExpect(MockMvcResultMatchers.jsonPath("filmAppearances").value(planet.getFilmAppearances()));
    }

    @Test
    @DisplayName("Deve retornar um planeta quando procurado por Name existente")
    void DeveRetornarPlanetaQuandoProcuradoPorNameExistenteTest() throws Exception {
        String name = "Tatooine";
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        BDDMockito.given(planetService.findByName(name)).willReturn(Optional.of(planet));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(PLANET_API_URL.concat("/name/" + name))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(planet.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(planet.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("climate").value(planet.getClimate()))
                .andExpect(MockMvcResultMatchers.jsonPath("terrain").value(planet.getTerrain()))
                .andExpect(MockMvcResultMatchers.jsonPath("filmAppearances").value(planet.getFilmAppearances()));
    }

    @Test
    @DisplayName("Deve lançar Exception quando tentar cadastrar um planeta sem os campos obrigatórios")
    void DeveLancarExceptionQuandoTentarCadastrarPlanetaSemCamposObrigatoriosTest() throws Exception {
        String json = new ObjectMapper().writeValueAsString(new CreatePlanetDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(PLANET_API_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(3)));
    }

    @Test
    @DisplayName("Deve cadastrar um planeta com sucesso")
    void DeveCadastrarPlanetaComSucessoTest() throws Exception {
        CreatePlanetDTO createPlanetDTO = CreatePlanetDTO.builder().name("Tatooine").climate("arid").terrain("desert")
                .build();
        Planet savedPlanet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        BDDMockito.given(planetService.save(Mockito.any(Planet.class))).willReturn(savedPlanet);

        String json = new ObjectMapper().writeValueAsString(createPlanetDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(PLANET_API_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value("60390340bd296130055aaa35"))
                .andExpect(MockMvcResultMatchers.jsonPath("name").value(createPlanetDTO.getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("climate").value(createPlanetDTO.getClimate()))
                .andExpect(MockMvcResultMatchers.jsonPath("terrain").value(createPlanetDTO.getTerrain()))
                .andExpect(MockMvcResultMatchers.jsonPath("filmAppearances").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("films", Matchers.hasSize(5)));
    }

    @Test
    @DisplayName("Deve lançar Exception quando tentar cadastrar um planeta já cadastrado")
    void DeveLancarExceptionQuandoTentarCadastrarPlanetaJaCadastradoTest() throws Exception {
        CreatePlanetDTO createPlanetDTO = CreatePlanetDTO.builder().name("Tatooine").climate("arid").terrain("desert")
                .build();
        String json = new ObjectMapper().writeValueAsString(createPlanetDTO);

        BDDMockito.given(planetService.save(Mockito.any(Planet.class)))
                .willThrow(new BusinessException("Planeta já cadastrado"));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(PLANET_API_URL)
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("message").value("Planeta já cadastrado"));
    }

    @Test
    @DisplayName("Deve retornar Not Found quando tentar atualizar um planeta inexistente")
    void DeveRetornarNotFoundQuandoTentarAtualizarPlanetaInexistenteTest() throws Exception {
        String id = "60390340bd296130055aaa35";
        UpdatePlanetDTO updatePlanetDTO = UpdatePlanetDTO.builder().climate("arid").terrain("desert").build();
        String json = new ObjectMapper().writeValueAsString(updatePlanetDTO);

        BDDMockito.given(planetService.findById(id)).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(PLANET_API_URL.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve lançar Exception quando tentar atualizar um planeta sem os campos obrigatórios")
    void DeveLancarExceptionQuandoTentarAtualizarPlanetaSemCamposObrigatoriosTest() throws Exception {
        String id = "60390340bd296130055aaa35";
        String json = new ObjectMapper().writeValueAsString(new UpdatePlanetDTO());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(PLANET_API_URL.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("status").value("BAD_REQUEST"))
                .andExpect(MockMvcResultMatchers.jsonPath("errors", Matchers.hasSize(2)));
    }

    @Test
    @DisplayName("Deve atualizar um planeta com sucesso")
    void DeveAtualizarPlanetaComSucessoTest() throws Exception {
        String id = "60390340bd296130055aaa35";
        UpdatePlanetDTO updatePlanetDTO = UpdatePlanetDTO.builder().climate("temperate")
                .terrain("grasslands, mountains").build();
        String json = new ObjectMapper().writeValueAsString(updatePlanetDTO);

        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
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

        BDDMockito.given(planetService.findById(id)).willReturn(Optional.of(planet));

        BDDMockito.given(planetService.update(planet)).willReturn(updatedPlanet);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.put(PLANET_API_URL.concat("/" + id))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON).content(json);

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("climate").value(updatePlanetDTO.getClimate()))
                .andExpect(MockMvcResultMatchers.jsonPath("terrain").value(updatePlanetDTO.getTerrain()))
                .andExpect(MockMvcResultMatchers.jsonPath("filmAppearances").value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("films", Matchers.hasSize(5)));
    }

    @Test
    @DisplayName("Deve retornar Not Found quando não encontrar um planeta para deletar")
    void DeveRetornarNotFoundQuandoNaoEncontrarPlanetaParaDeletarTest() throws Exception {
        String id = "60390340bd296130055aaa35";
        BDDMockito.given(planetService.findById(id)).willReturn(Optional.empty());

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(PLANET_API_URL.concat("/" + id));

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um planeta com sucesso")
    void DeveDeletarPlanetaComSucessoTest() throws Exception {
        String id = "60390340bd296130055aaa35";
        Planet planet = Planet.builder().id("60390340bd296130055aaa35").name("Tatooine").climate("arid")
                .terrain("desert").filmAppearances(5)
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();

        BDDMockito.given(planetService.findById(id)).willReturn(Optional.of(planet));

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.delete(PLANET_API_URL.concat("/" + id));

        mockMvc.perform(request).andExpect(MockMvcResultMatchers.status().isNoContent());
    }
}
