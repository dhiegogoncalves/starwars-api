package com.project.starwars.repositories.api;

import java.util.Optional;

import com.project.starwars.dtos.api.ApiPlanetDTO;
import com.project.starwars.dtos.api.ApiResultDTO;
import com.project.starwars.exceptions.ApiErrorException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class SwApiRepository {

    @Value("${swapi-url}")
    private String swApiUrl;

    private final RestTemplate restTemplate;

    public ApiPlanetDTO findPlanetByName(String name) {
        String apiUrl = swApiUrl + "planets/?search=" + name;

        try {
            ResponseEntity<ApiResultDTO> responseEntity = restTemplate.getForEntity(apiUrl, ApiResultDTO.class);

            ApiPlanetDTO apiPlanetDTO = responseEntity.getBody().getResults().isEmpty() ? null
                    : responseEntity.getBody().getResults().get(0);

            return apiPlanetDTO;
        } catch (Exception e) {
            throw new ApiErrorException("Não foi possível verificar o nome do planeta no universo de Star Wars");
        }
    }
}
