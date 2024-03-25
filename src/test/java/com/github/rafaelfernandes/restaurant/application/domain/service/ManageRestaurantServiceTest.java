package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.common.exception.RestaurantDuplicateException;
import com.github.rafaelfernandes.common.exception.RestaurantNotFoundException;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.out.ManageRestaurantPort;
import com.github.rafaelfernandes.service.application.domain.model.Service;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import util.GenerateData;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    @Nested
    class FindById {

        @Test
        void validateSuccessFindById(){

            // Arrange

            var restaurantRequest = GenerateData.createRestaurant();

            when(port.findById(any(Restaurant.RestaurantId.class))).thenReturn(Optional.of(restaurantRequest));

            // Act

            var restaurantData = service.findById(restaurantRequest.getRestaurantId());

            // Assert

            assertThat(restaurantData).isNotNull();
            assertThat(restaurantData.getRestaurantId()).isEqualTo(restaurantRequest.getRestaurantId());
            assertThat(restaurantData.getName()).isEqualTo(restaurantRequest.getName());

            verify(port, times(1)).findById(any());

        }

        @Test
        void validateNotFound(){

            // Arrange

            var restaurantRequest = GenerateData.createRestaurant();

            when(port.findById(any(Restaurant.RestaurantId.class))).thenReturn(Optional.empty());

            // Act | Assert

            assertThatThrownBy(() -> {
                service.findById(restaurantRequest.getRestaurantId());
            })
                    .isInstanceOf(RestaurantNotFoundException.class)
                    .hasMessage("Restaurante(s) não existe!")
            ;

            verify(port, times(1)).findById(any());

        }

    }

    @Nested
    class FindAllBy {



        @BeforeEach
        void setUpFindAll(){



        }

        @Test
        void findAllByName(){

            // Arrange

            var restaurant = GenerateData.createRestaurant();
            var restaurants = new ArrayList<Restaurant>(){{
               add(restaurant);
            }};

            when(port.findAllBy(anyString(), anyString(), anyList())).thenReturn(restaurants);

            // Act

            var list = service.findAllBy(restaurant.getName(), "", new ArrayList<>());

            // Assert

            assertThat(list).hasSize(1).contains(restaurant);
            verify(port.findAllBy(any(), null, null), times(1));

        }

    }
}