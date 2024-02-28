package com.github.rafaelfernandes.restaurant.application.domain.model;

import com.github.rafaelfernandes.restaurant.application.model.Restaurant;
import com.github.rafaelfernandes.restaurant.common.State;
import com.github.rafaelfernandes.restaurant.util.GenerateData;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));

    @Nested
    @DisplayName("Tests of Create method")
    class Create {
        @Test
        void createRestaurantWithTables() {
            String name = faker.restaurant().name();


            Restaurant.Address address = new Restaurant.Address(
                    faker.address().streetAddress(),
                    Integer.valueOf(faker.address().streetAddressNumber()),
                    faker.address().secondaryAddress(),
                    "Medicina",
                    faker.address().city(),
                    State.valueOf(faker.address().stateAbbr()));

            Restaurant restaurant = Restaurant.create(name, address);

            assertNotNull(restaurant);
            assertNotNull(restaurant.getRestaurantId());
            assertEquals(name, restaurant.getName());
            assertEquals(address, restaurant.getAddress());
            assertNotNull(restaurant.getRegister());
            assertNotNull(restaurant.getOpeningHours());
            assertEquals(0, restaurant.getTables());
        }

        @Test
        void createRestaurantWithDefaultOpeningHours() {
            String name = "Test Restaurant";
            Restaurant.Address address = new Restaurant.Address(
                    faker.address().streetAddress(),
                    Integer.valueOf(faker.address().streetAddressNumber()),
                    faker.address().secondaryAddress(),
                    "Medicina",
                    faker.address().city(),
                    State.valueOf(faker.address().stateAbbr()));

            Restaurant restaurant = Restaurant.create(name, address);

            assertNotNull(restaurant.getOpeningHours());
            assertEquals(DayOfWeek.values().length, restaurant.getOpeningHours().size());

            for (Restaurant.OpeningHour openingHour : restaurant.getOpeningHours()) {
                assertEquals(LocalTime.of(9, 0), openingHour.getStart());
                assertEquals(LocalTime.of(18, 0), openingHour.getEnd());
            }
        }
    }

    @Nested
    @DisplayName("Tests of Of method")
    class Of {


        @Test
        void createRestaurantUsingOf() {
            UUID restaurantId = UUID.randomUUID();
            String name = "Test Restaurant";
            Restaurant.Address address = new Restaurant.Address(
                    faker.address().streetAddress(),
                    Integer.valueOf(faker.address().streetAddressNumber()),
                    faker.address().secondaryAddress(),
                    "Medicina",
                    faker.address().city(),
                    State.valueOf(faker.address().stateAbbr()));
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
            assertEquals(numberOfTables, restaurant.getTables());
        }

        @Test
        void createRestaurantWithNullOpeningHours() {
            UUID restaurantId = UUID.randomUUID();
            String name = "Test Restaurant";
            Restaurant.Address address = new Restaurant.Address(
                    faker.address().streetAddress(),
                    Integer.valueOf(faker.address().streetAddressNumber()),
                    faker.address().secondaryAddress(),
                    "Medicina",
                    faker.address().city(),
                    State.valueOf(faker.address().stateAbbr()));
            LocalDateTime register = LocalDateTime.now();
            Integer numberOfTables = 5;

            Restaurant restaurant = Restaurant.of(restaurantId, name, address, register, null, numberOfTables, null);

            assertNotNull(restaurant);
            assertEquals(restaurantId, restaurant.getRestaurantId().getValue());
            assertEquals(name, restaurant.getName());
            assertEquals(address, restaurant.getAddress());
            assertEquals(register, restaurant.getRegister());
            assertNull(restaurant.getOpeningHours());
            assertEquals(numberOfTables, restaurant.getTables());
        }
    }

    @Nested
    @DisplayName("Test methods Change/Add/Remove")
    class ChangeAddRemove {

        Restaurant restaurant;

        @BeforeEach
        void generateRestaurant(){
            this.restaurant = GenerateData.createRestaurant();
        }

        @Test
        void changeNameRestaurant(){

            String newName = faker.restaurant().name();

            Restaurant changeName = this.restaurant.changeName(newName);

            assertEquals(this.restaurant.getRestaurantId(), changeName.getRestaurantId());
            assertEquals(newName, changeName.getName());

        }

        @Test
        void changeAddressRestaurant(){

            Restaurant.Address newAddress = GenerateData.generateAddress();

            Restaurant changeAddress = restaurant.changeAddress(newAddress);

            assertEquals(this.restaurant.getRestaurantId(), changeAddress.getRestaurantId());
            assertEquals(newAddress.getStreet(), changeAddress.getAddress().getStreet());
            assertEquals(newAddress.getNumber(), changeAddress.getAddress().getNumber());
            assertEquals(newAddress.getAddittionalDetails(), changeAddress.getAddress().getAddittionalDetails());
            assertEquals(newAddress.getNeighborhood(), changeAddress.getAddress().getNeighborhood());
            assertEquals(newAddress.getCity(), changeAddress.getAddress().getCity());
            assertEquals(newAddress.getState(), changeAddress.getAddress().getState());


        }

        @Test
        void addOpeningHourRestaurant(){

            Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour(
                    DayOfWeek.FRIDAY,
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 30)
                    );

            Restaurant addOpeningHourRestauran = this.restaurant.addOpeningHours(openingHour);

            assertEquals(this.restaurant.getRestaurantId(), addOpeningHourRestauran.getRestaurantId());

            assertTrue(addOpeningHourRestauran.getOpeningHours().contains(openingHour));
        }

        @Test
        void removeOpeningHourRestaurant(){

            Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour(
                    DayOfWeek.FRIDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(18, 0)
            );

            Restaurant removeOpeningHourRestaurant = this.restaurant.removeOpeningHours(openingHour);

            assertEquals(this.restaurant.getRestaurantId(), removeOpeningHourRestaurant.getRestaurantId());

            assertFalse(removeOpeningHourRestaurant.getOpeningHours().contains(openingHour));
        }

        @Test
        void changeNumberOfTablesRestaurant(){

            Integer numberOfTables = 10;

            Restaurant changeNumberOfTablesRestaurant = this.restaurant.changeNumberOfTables(numberOfTables);

            assertEquals(this.restaurant.getRestaurantId(), changeNumberOfTablesRestaurant.getRestaurantId());

            assertEquals(numberOfTables, changeNumberOfTablesRestaurant.getTables());
        }



    }



}
