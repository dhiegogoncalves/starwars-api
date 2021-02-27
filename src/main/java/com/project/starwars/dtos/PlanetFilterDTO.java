package com.project.starwars.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlanetFilterDTO {

    private String name;
    private String climate;
    private String terrain;
    private Integer filmAppearances;
}
