package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.common.exception.RestaurantDuplicateException;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.out.ManageRestaurantPort;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.GenerateData;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManageRestaurantServiceTest {

    private final ManageRestaurantPort port = Mockito.mock(ManageRestaurantPort.class);
    private final ManageRestaurantService service = new ManageRestaurantService(port);

    @Nested
    class Create {

        @Test
        void validateCreateSuccessRestaurant(){

            var restaurantRequest = GenerateData.createRestaurant();

            when(port.existsName(any(String.class))).thenReturn(Boolean.FALSE);

            when(port.save(any(Restaurant.class))).thenReturn(restaurantRequest);

            var saved = service.create(restaurantRequest);

            assertThat(saved).isNotNull();
            assertThat(saved.id()).isNotNull();

            verify(port, times(1)).existsName(any());
            verify(port, times(1)).save(any());

        }

        @Test
        void validateCreateDuplicateRestaurant(){

            var restaurantRequest = GenerateData.createRestaurant();

            when(port.existsName(any(String.class))).thenReturn(Boolean.TRUE);

            assertThatThrownBy(() -> {
                service.create(restaurantRequest);
            })
                    .isInstanceOf(RestaurantDuplicateException.class)
                    .hasMessage("Nome já cadastrado!")
            ;

            verify(port, times(1)).existsName(any());
            verify(port, times(0)).save(any());

        }

    }

    @Test
    void findById() {
    }

    @Test
    void findAllBy() {
    }
}