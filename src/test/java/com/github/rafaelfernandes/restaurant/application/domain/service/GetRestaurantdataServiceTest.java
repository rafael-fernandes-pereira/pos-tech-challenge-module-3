package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.out.GetRestaurantPort;
import com.github.rafaelfernandes.common.exception.RestaurantNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.GenerateData;

import java.util.ArrayList;
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



    @Nested
    class FindAllBy {

        @Test
        void validateFindSuccess(){
            var restaurants = new ArrayList<Restaurant>();
            restaurants.add(GenerateData.createRestaurant());
            restaurants.add(GenerateData.createRestaurant());
            restaurants.add(GenerateData.createRestaurant());

            when(getRestaurantPort.findAllBy(any(String.class), any(String.class), anyList()))
                    .thenReturn(restaurants);


            var restaurantGetData = getRestaurantdataService.findAllBy("Dio Mio", "Rua", new ArrayList<>());

            assertThat(restaurantGetData).hasSize(3);

            verify(getRestaurantPort, times(1)).findAllBy(any(String.class), any(String.class), anyList());

        }

        @Test
        void validateEmpty(){
            when(getRestaurantPort.findAllBy(any(String.class), any(String.class), anyList()))
                    .thenReturn(new ArrayList<Restaurant>());


            assertThatThrownBy(() -> {
                getRestaurantdataService.findAllBy("Dio Mio", "Rua", new ArrayList<>());
            })
                    .isInstanceOf(RestaurantNotFoundException.class)
                    .hasMessage("Restaurante(s) não existe!");

            verify(getRestaurantPort, times(1)).findAllBy(any(String.class), any(String.class), anyList());
        }



    }


}