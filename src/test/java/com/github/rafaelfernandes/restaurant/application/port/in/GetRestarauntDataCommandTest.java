package com.github.rafaelfernandes.restaurant.application.port.in;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class GetRestarauntDataCommandTest {

    @Nested
    class RestaurantId {

        @Test
        void validateSucess(){

            String uuid = "052b8da7-52d4-435f-b753-a3bb2ea51ae4";

            GetRestarauntDataCommand command = new GetRestarauntDataCommand(uuid);

            assertThat(command.restaurantId()).isEqualTo(uuid);

        }


        @ParameterizedTest
        @ValueSource(strings = {"", "uuid-error"})
        void validateInvalidUUI(String restaurantId){
            assertThatThrownBy(() -> {
                new GetRestarauntDataCommand(restaurantId);
            })
                    .isInstanceOf(ConstraintViolationException.class)
                    .hasMessageContainingAll("restaurantId: O campo deve ser do tipo UUID")
            ;
        }

    }


}