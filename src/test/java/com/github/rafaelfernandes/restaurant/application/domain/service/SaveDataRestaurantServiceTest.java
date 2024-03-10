package com.github.rafaelfernandes.restaurant.application.domain.service;


import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.out.CreateRestaurantPort;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantDuplicateException;
import util.GenerateData;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import static org.mockito.BDDMockito.*;

class SaveDataRestaurantServiceTest {

    private final CreateRestaurantPort createRestaurantPort
            = Mockito.mock(CreateRestaurantPort.class);

    private final SaveDataRestaurantService saveDataRestaurantService
            = new SaveDataRestaurantService(createRestaurantPort);


    @Test
    void validateCreateRestaurant(){

        var command = GenerateData.createRestaurantCommand();

        var restaurantId = new Restaurant.RestaurantId(UUID.randomUUID());

        when(createRestaurantPort.create(any(Restaurant.class)))
                .thenReturn(restaurantId);

        Restaurant.RestaurantId savedId = saveDataRestaurantService.create(command);

        assertThat(restaurantId).isEqualTo(savedId);
        verify(createRestaurantPort, times(1)).create(any());

    }

    @Test
    void validateDuplicateRestaurant(){

        var command = GenerateData.createRestaurantCommand();
        when(createRestaurantPort.create(any(Restaurant.class)))
                .thenThrow(RestaurantDuplicateException.class);

        assertThatThrownBy(() -> {
            saveDataRestaurantService.create(command);
        })
                .isInstanceOf(RestaurantDuplicateException.class)
                .hasMessage("Nome já cadastrado!")
        ;

    }



}