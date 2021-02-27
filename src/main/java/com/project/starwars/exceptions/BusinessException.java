package com.project.starwars.exceptions;

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 8661185252685806441L;

    public BusinessException(String message) {
        super(message);
    }
}
