package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.response.RestaurantError;
import com.github.rafaelfernandes.restaurant.exception.RestaurantDuplicateException;
import com.github.rafaelfernandes.restaurant.exception.RestaurantNotFoundException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = RestaurantExceptionHandler.class)
public class RestaurantExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<RestaurantError> restaurantErrorValidation(ValidationException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RestaurantError(exception.getMessage()));
    }

    @ExceptionHandler({RestaurantDuplicateException.class})
    public ResponseEntity<RestaurantError> restaurantErrorValidation(RestaurantDuplicateException exception){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new RestaurantError(exception.getMessage()));
    }

    @ExceptionHandler({RestaurantNotFoundException.class})
    public ResponseEntity<RestaurantError> restaurantErrorValidation(RestaurantNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new RestaurantError(exception.getMessage()));
    }


    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<RestaurantError> restaurantErrorValidation(IllegalArgumentException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new RestaurantError(exception.getMessage()));
    }



}
