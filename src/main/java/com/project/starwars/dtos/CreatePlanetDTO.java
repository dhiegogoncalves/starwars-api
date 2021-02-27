package com.project.starwars.dtos;

import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreatePlanetDTO {

    @NotEmpty(message = "Name é um campo obrigatório")
    private String name;

    @NotEmpty(message = "Climate é um campo obrigatório")
    private String climate;

    @NotEmpty(message = "Terrain é um campo obrigatório")
    private String terrain;
}
