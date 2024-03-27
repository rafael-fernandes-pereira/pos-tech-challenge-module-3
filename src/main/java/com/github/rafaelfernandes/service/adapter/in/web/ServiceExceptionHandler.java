package com.github.rafaelfernandes.service.adapter.in.web;

import com.github.rafaelfernandes.common.response.ErrorResponse;
import com.github.rafaelfernandes.restaurant.exception.RestaurantNotFoundException;
import com.github.rafaelfernandes.service.exception.ServiceDuplicateException;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackageClasses = ServiceController.class)
public class ServiceExceptionHandler {

    @ExceptionHandler({ValidationException.class})
    public ResponseEntity<ErrorResponse> restaurantErrorValidation(ValidationException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({RestaurantNotFoundException.class})
    public ResponseEntity<ErrorResponse> restaurantErrorValidation(RestaurantNotFoundException exception){
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({ServiceDuplicateException.class})
    public ResponseEntity<ErrorResponse> serviceDuplicate(ServiceDuplicateException exception){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<ErrorResponse> restaurantErrorValidation(IllegalArgumentException exception){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse(exception.getMessage()));
    }



}
