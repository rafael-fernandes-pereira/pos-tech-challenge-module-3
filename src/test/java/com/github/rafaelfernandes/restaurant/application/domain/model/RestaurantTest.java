package com.github.rafaelfernandes.restaurant.application.domain.model;

import com.github.rafaelfernandes.restaurant.application.model.Restaurant;
import com.github.rafaelfernandes.restaurant.util.GenerateData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {

    @Nested
    class create {
        @Test
        void createRestaurantWithTables() {
            String name = "Test Restaurant";
            String address = "Test Address";
            Integer numberOfTables = 5;

            Restaurant restaurant = Restaurant.create(name, address, numberOfTables);

            assertNotNull(restaurant);
            assertNotNull(restaurant.getRestaurantId());
            assertEquals(name, restaurant.getName());
            assertEquals(address, restaurant.getAddress());
            assertNotNull(restaurant.getRegister());
            assertNotNull(restaurant.getOpeningHours());
            assertEquals(numberOfTables, restaurant.getTables().size());
        }

        @Test
        void createRestaurantWithDefaultOpeningHours() {
            String name = "Test Restaurant";
            String address = "Test Address";
            Integer numberOfTables = 5;

            Restaurant restaurant = Restaurant.create(name, address, numberOfTables);

            assertNotNull(restaurant.getOpeningHours());
            assertEquals(DayOfWeek.values().length, restaurant.getOpeningHours().size());

            for (Restaurant.OpeningHour openingHour : restaurant.getOpeningHours()) {
                assertEquals(LocalDateTime.of(2024, 1, 1, 9, 0), openingHour.getStart());
                assertEquals(LocalDateTime.of(2024, 1, 1, 18, 0), openingHour.getEnd());
            }
        }

        @Test
        void createRestaurantUsingOf() {
            UUID restaurantId = UUID.randomUUID();
            String name = "Test Restaurant";
            String address = "Test Address";
            LocalDateTime register = LocalDateTime.now();
            List<Restaurant.OpeningHour> openingHours = GenerateData.createDefaultOpeningHours();
            Integer numberOfTables = 5;

            Restaurant restaurant = Restaurant.of(restaurantId, name, address, register, openingHours, numberOfTables, null);

            assertNotNull(restaurant);
            assertEquals(restaurantId, restaurant.getRestaurantId().getValue());
            assertEquals(name, restaurant.getName());
            assertEquals(address, restaurant.getAddress());
            assertEquals(register, restaurant.getRegister());
            assertEquals(openingHours, restaurant.getOpeningHours());
            assertEquals(numberOfTables, restaurant.getTables().size());
        }

        @Test
        void createRestaurantWithNullOpeningHours() {
            UUID restaurantId = UUID.randomUUID();
            String name = "Test Restaurant";
            String address = "Test Address";
            LocalDateTime register = LocalDateTime.now();
            Integer numberOfTables = 5;

            Restaurant restaurant = Restaurant.of(restaurantId, name, address, register, null, numberOfTables, null);

            assertNotNull(restaurant);
            assertEquals(restaurantId, restaurant.getRestaurantId().getValue());
            assertEquals(name, restaurant.getName());
            assertEquals(address, restaurant.getAddress());
            assertEquals(register, restaurant.getRegister());
            assertNull(restaurant.getOpeningHours());
            assertEquals(numberOfTables, restaurant.getTables().size());
        }
    }



}
