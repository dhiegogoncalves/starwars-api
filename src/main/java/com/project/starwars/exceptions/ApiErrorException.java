package com.project.starwars.exceptions;

public class ApiErrorException extends RuntimeException {

    private static final long serialVersionUID = -8182930330075834299L;

    public ApiErrorException(String message) {
        super(message);
    }
}