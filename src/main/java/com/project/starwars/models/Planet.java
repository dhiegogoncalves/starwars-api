package com.project.starwars.models;

import java.util.List;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Planet {

    @Id
    private String id;
    private String name;
    private String climate;
    private String terrain;
    private Integer filmAppearances;
    private List<String> films;
}
