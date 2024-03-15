package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.restaurant.application.port.in.GetRestarauntDataCommand;
import com.github.rafaelfernandes.restaurant.application.port.out.GetRestaurantPort;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantNotFoundException;
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

        var command = GenerateData.createGetRestarauntDataCommandId();

        var restaurant = GenerateData.createRestaurant();

        var restaurantId = restaurant.getRestaurantId().getValue();

        when(getRestaurantPort.findById(any(UUID.class)))
                .thenReturn(Optional.of(restaurant));


        var restaurantGetData = getRestaurantdataService.findBy(command);

        assertThat(restaurantGetData).isPresent();
        assertThat(restaurantId).isEqualTo(restaurantGetData.get().getRestaurantId().getValue());

        verify(getRestaurantPort, times(1)).findById(any(UUID.class));

    }

    @Test
    void validateEmptyResult(){

        var command = GenerateData.createGetRestarauntDataCommandId();

        when(getRestaurantPort.findById(any(UUID.class)))
                .thenReturn(Optional.empty());


        assertThatThrownBy(() -> {
            getRestaurantdataService.findBy(command);
        })
                .isInstanceOf(RestaurantNotFoundException.class)
                .hasMessage("Restaurante(s) não existe!");

        verify(getRestaurantPort, times(1)).findById(any(UUID.class));

    }

    @Test
    void validateRestaurantIdEmpty(){

        var command = new GetRestarauntDataCommand(null);

        assertThatThrownBy(() -> {
            getRestaurantdataService.findBy(command);
        })
                .isInstanceOf(RestaurantNotFoundException.class)
                .hasMessage("Restaurante(s) não existe!");

        verify(getRestaurantPort, times(0)).findById(any(UUID.class));

    }


}