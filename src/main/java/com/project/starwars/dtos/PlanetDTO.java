package com.project.starwars.dtos;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlanetDTO {

    private String id;
    private String name;
    private String climate;
    private String terrain;
    private Integer filmAppearances;
    private List<String> films;
}
