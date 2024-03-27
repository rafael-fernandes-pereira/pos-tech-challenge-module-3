package com.github.rafaelfernandes.service.application.domain.service;

import com.github.rafaelfernandes.common.enums.Cuisine;
import com.github.rafaelfernandes.service.exception.ServiceDuplicateException;
import com.github.rafaelfernandes.restaurant.application.port.in.ManageRestaurantUseCase;
import com.github.rafaelfernandes.service.application.domain.model.Service;
import com.github.rafaelfernandes.service.application.port.out.ManageServicePort;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class ManageServiceServiceTest {

    private final ManageServicePort manageServicePort = Mockito.mock(ManageServicePort.class);
    private final ManageRestaurantUseCase manageRestaurantUseCase = Mockito.mock(ManageRestaurantUseCase.class);
    private final ManageServiceService manageReservationUseCase = new ManageServiceService(manageServicePort, manageRestaurantUseCase);

    Restaurant.Address address = Mockito.mock(Restaurant.Address.class);
    Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour(
            DayOfWeek.MONDAY.name(),
            LocalTime.of(10, 0),
            LocalTime.of(15, 10)
    );
    ArrayList<Restaurant.OpeningHour> openingHours = new ArrayList<>(){{
        add(openingHour);
    }};
    ArrayList<Restaurant.Cuisine> cuisines = new ArrayList<>(){{
        add(new Restaurant.Cuisine(Cuisine.BRAZILIAN.getName()));
    }};
    String name = "Chorume Gastrobar";

    Integer tables = 1;
    Restaurant restaurant = new Restaurant(name, address,openingHours, cuisines, tables);

    LocalDate date = LocalDate.now();


    @Nested
    class Create {

        @Test
        void validateServiceExists(){

            Mockito.when(manageServicePort.existsService(any(Restaurant.RestaurantId.class), any(Restaurant.OpeningHour.class), any(LocalDate.class))).thenReturn(true);

            assertThatCode(() -> {
                manageReservationUseCase.create(restaurant.getRestaurantId(), openingHour, date, tables);
            })
                    .isInstanceOf(ServiceDuplicateException.class)
                    .hasMessageContaining("Serviço para essa data e para esse horário já existe!");

        }

        @Test
        void testCreate() {

            // Arrange

            Mockito.when(manageServicePort.existsService(any(Restaurant.RestaurantId.class), any(Restaurant.OpeningHour.class), any(LocalDate.class))).thenReturn(false);
            Mockito.when(manageRestaurantUseCase.findById(any(Restaurant.RestaurantId.class))).thenReturn(restaurant);

            var service = new Service(restaurant, openingHour, date, tables);

            Mockito.when(manageServicePort.save(any(Service.class))).thenReturn(service);

            // Act

            Service.ServiceId serviceId = manageReservationUseCase.create(restaurant.getRestaurantId(), openingHour, date, tables);

            // Assert
            assertNotNull(serviceId);

        }

    }



}
