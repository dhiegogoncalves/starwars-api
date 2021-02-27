package com.project.starwars;

import java.time.Duration;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@SpringBootApplication
public class StarWarsApplication {

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().setConnectTimeout(Duration.ofSeconds(3)).setReadTimeout(Duration.ofSeconds(3))
                .build();
    }

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.project.starwars.controllers"))
                .paths(PathSelectors.any()).build()
                .apiInfo(new ApiInfoBuilder().title("Star Wars API")
                        .description("API dos planetas do universo do Star Wars").version("1.0")
                        .contact(new Contact("Dhiego Gonçalves da Silva", "http://github.com/dhiegogoncalves",
                                "dhhiego@gmail.com"))
                        .build());
    }

    public static void main(String[] args) {
        SpringApplication.run(StarWarsApplication.class, args);
    }

}
