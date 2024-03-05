package com.github.rafaelfernandes.restaurant.common.exception;

public class RestaurantDuplicateException extends RestaurantException {

    private static final String ERROR = "Nome já cadastrado!";

    public RestaurantDuplicateException() {
        super(ERROR);
    }

    @Override
    public String getMessage() {
        return RestaurantDuplicateException.ERROR;
    }
}
