package com.project.starwars.repositories.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import com.project.starwars.dtos.api.ApiPlanetDTO;
import com.project.starwars.dtos.api.ApiResultDTO;
import com.project.starwars.exceptions.ApiErrorException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class SwApiRepositoryTest {

    SwApiRepository swApiRepository;

    @MockBean
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        this.swApiRepository = new SwApiRepository(restTemplate);
        ReflectionTestUtils.setField(swApiRepository, "swApiUrl", "https://swapi.dev/api/");
    }

    @Test
    @DisplayName("Deve retornar Null quando não encontrar um planeta com o Name na Api")
    void DeveRetornarNullQuandoNaoEncontrarPlanetaComNameNaApiTest() {
        String name = "name";
        ApiResultDTO apiResultDTO = ApiResultDTO.builder().results(new ArrayList<ApiPlanetDTO>()).build();

        Mockito.when(restTemplate.getForEntity("https://swapi.dev/api/planets/?search=" + name, ApiResultDTO.class))
                .thenReturn(new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.OK));

        ApiPlanetDTO apiPlanetDTO = swApiRepository.findPlanetByName(name);

        Assertions.assertThat(apiPlanetDTO).isNull();
    }

    @Test
    @DisplayName("Deve lançar Exception quando acontecer algum erro na consulta da API")
    void DevelancarExceptionQuandoAcontecerAlgumErroNaConsultaDaApiTest() {
        String name = "name";

        Mockito.when(restTemplate.getForEntity("https://swapi.dev/api/planets/?search=" + name, ApiResultDTO.class))
                .thenReturn(new ResponseEntity(null, HttpStatus.BAD_REQUEST));

        Assertions.assertThatExceptionOfType(ApiErrorException.class).isThrownBy(() -> {
            swApiRepository.findPlanetByName(name);
        }).withMessage("Não foi possível verificar o nome do planeta no universo de Star Wars");
    }

    @Test
    @DisplayName("Deve retornar um planeta quando procurar por Name existente")
    void DeveRetornarPlanetaQuandoProcurarPorNameExistenteTest() {
        String name = "tatooine";
        ApiPlanetDTO result = ApiPlanetDTO.builder().name("Tatooine").climate("arid").terrain("desert")
                .films(Arrays.asList("http://swapi.dev/api/films/1/", "http://swapi.dev/api/films/3/",
                        "http://swapi.dev/api/films/4/", "http://swapi.dev/api/films/5/",
                        "http://swapi.dev/api/films/6/"))
                .build();
        List<ApiPlanetDTO> apiPlanetDTOList = new ArrayList<ApiPlanetDTO>();
        apiPlanetDTOList.add(result);
        ApiResultDTO apiResultDTO = ApiResultDTO.builder().results(apiPlanetDTOList).build();

        Mockito.when(restTemplate.getForEntity("https://swapi.dev/api/planets/?search=" + name, ApiResultDTO.class))
                .thenReturn(new ResponseEntity<ApiResultDTO>(apiResultDTO, HttpStatus.OK));

        ApiPlanetDTO apiPlanetDTO = swApiRepository.findPlanetByName(name);

        Assertions.assertThat(apiPlanetDTO).isNotNull();
        Assertions.assertThat(apiPlanetDTO.getName()).isEqualTo(result.getName());
        Assertions.assertThat(apiPlanetDTO.getClimate()).isEqualTo(result.getClimate());
        Assertions.assertThat(apiPlanetDTO.getTerrain()).isEqualTo(result.getTerrain());
        Assertions.assertThat(apiPlanetDTO.getFilms()).isEqualTo(result.getFilms());
    }
}
