package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.response.RestaurantError;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantDuplicateException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = RestaurantExceptionHandler.class)
public class RestaurantExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<RestaurantError> restaurantErrorValidation(ConstraintViolationException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RestaurantError(exception.getMessage()));
    }

    @ExceptionHandler({RestaurantDuplicateException.class})
    public ResponseEntity<RestaurantError> restaurantErrorValidation(RestaurantDuplicateException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RestaurantError(exception.getMessage()));
    }

}
