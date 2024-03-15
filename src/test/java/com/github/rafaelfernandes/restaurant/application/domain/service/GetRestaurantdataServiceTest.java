package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.GetRestarauntDataCommand;
import com.github.rafaelfernandes.restaurant.application.port.out.GetRestaurantPort;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.GenerateData;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetRestaurantdataServiceTest {

    private final GetRestaurantPort getRestaurantPort
            = Mockito.mock(GetRestaurantPort.class);

    private final GetRestaurantdataService getRestaurantdataService
            = new GetRestaurantdataService(getRestaurantPort);

    @Test
    void validateSuccess() {

        var restaurant = GenerateData.createRestaurant();

        var restaurantId = restaurant.getRestaurantId();

        when(getRestaurantPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(restaurant));


        var restaurantGetData = getRestaurantdataService.findById(restaurantId);

        assertThat(restaurantGetData).isPresent();
        assertThat(restaurantId).isEqualTo(restaurantGetData.get().getRestaurantId());

        verify(getRestaurantPort, times(1)).findById(any(UUID.class));

    }

    @Test
    void validateEmptyResult(){

        when(getRestaurantPort.findById(any(UUID.class)))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> {
            getRestaurantdataService.findById(new Restaurant.RestaurantId(UUID.randomUUID().toString()));
        })
                .isInstanceOf(RestaurantNotFoundException.class)
                .hasMessage("Restaurante(s) não existe!");

        verify(getRestaurantPort, times(1)).findById(any(UUID.class));

    }

    @Test
    void validateRestaurantIdEmpty(){

        assertThatThrownBy(() -> {
            getRestaurantdataService.findById(new Restaurant.RestaurantId(""));
        })
                .isInstanceOf(ConstraintViolationException.class)
                .hasMessage("id: O campo deve ser do tipo UUID");

        verify(getRestaurantPort, times(0)).findById(any(UUID.class));

    }


}