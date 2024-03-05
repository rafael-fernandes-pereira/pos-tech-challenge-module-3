package com.github.rafaelfernandes.restaurant.common.exception;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public class RestaurantException extends RuntimeException{

    private final Set<String> errors;

    public RestaurantException(String message) {
        super(message);
        this.errors = new HashSet<>();
        this.errors.add(message);
    }
}
