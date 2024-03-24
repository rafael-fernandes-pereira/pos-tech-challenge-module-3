package com.github.rafaelfernandes.service.application.domain.model;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

class ServiceTest {


    @Test
    public void testValidConstructor() {
        // Arrange
        String reservationId = "123e4567-e89b-12d3-a456-556642440000";
        Restaurant.RestaurantId restaurantId = new Restaurant.RestaurantId("987e6543-e89b-12d3-a456-556642440000");
        Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour(DayOfWeek.MONDAY.name(), LocalTime.of(10,0), LocalTime.of(11,30));

        LocalDate date = LocalDate.now();
        Integer missingTables = 5;

        // Act
        Service reservation = new Service(reservationId, restaurantId, openingHour, date, missingTables);

        // Assert
        assertNotNull(reservation);
        assertEquals(reservationId, reservation.getReservationId().id());
        assertEquals(restaurantId, reservation.getRestaurantId());
        assertEquals(openingHour, reservation.getOpeningHour());
        assertEquals(date, reservation.getDate());
        assertEquals(missingTables, reservation.getTables());
    }


    @Test
    public void testConstructorWithInvalidUUID() {
        String reservationId = "invalid-uuid";
        Restaurant.RestaurantId restaurantId = Mockito.mock(Restaurant.RestaurantId.class);
        Restaurant.OpeningHour openingHour = Mockito.mock(Restaurant.OpeningHour.class);
        LocalDate date = LocalDate.now();
        Integer missingTables = 1;

        assertThrows(ConstraintViolationException.class, () -> {
            new Service(reservationId, restaurantId, openingHour, date, missingTables);
        });
    }

    @Test
    public void testConstructorWithMissingTablesLessThanOne() {
        String reservationId = "123e4567-e89b-12d3-a456-426614174000";
        Restaurant.RestaurantId restaurantId = Mockito.mock(Restaurant.RestaurantId.class);
        Restaurant.OpeningHour openingHour = Mockito.mock(Restaurant.OpeningHour.class);
        LocalDate date = LocalDate.now();
        Integer missingTables = 0;

        assertThrows(ConstraintViolationException.class, () -> {
            new Service(reservationId, restaurantId, openingHour, date, missingTables);
        });
    }

}