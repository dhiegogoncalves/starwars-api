package com.project.starwars.dtos.api;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiPlanetDTO {

    private String name;
    private String climate;
    private String terrain;
    private List<String> films;
}
