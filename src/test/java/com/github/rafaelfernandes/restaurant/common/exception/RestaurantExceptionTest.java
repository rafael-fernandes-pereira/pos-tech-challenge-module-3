package com.github.rafaelfernandes.restaurant.common.exception;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantExceptionTest {

    @Test
    void validateMessageError(){

        RestaurantDuplicateException exception = new RestaurantDuplicateException();

        assertThat(exception.getErrors()).anyMatch(erro -> erro.equalsIgnoreCase("Nome já cadastrado!"));

    }

}
