package com.github.rafaelfernandes.restaurant.exception;

public class RestaurantDuplicateException extends RuntimeException{

    private static final String ERROR = "Nome já cadastrado!";

    public RestaurantDuplicateException() {
        super(ERROR);
    }

    @Override
    public String getMessage() {
        return ERROR;
    }
}
