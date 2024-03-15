package com.github.rafaelfernandes.restaurant.domain;

import com.github.rafaelfernandes.restaurant.common.enums.Cuisine;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import util.GenerateData;
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

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));

    @Nested
    class Validate {

        @Nested
        class Name {

            Restaurant.Address address =  GenerateData.generateAddress();

            @ParameterizedTest
            @ValueSource(strings = {"Rafael Fernandes Pereira", "Dio", "Gabriela Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira"})
            void validateSucessName(String name){

                var restaurant = new Restaurant(name, address);

                assertEquals(name, restaurant.getName());

            }

            @Test
            void validateNullName(){

                assertThatCode(() -> {
                    new Restaurant(null, address);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("name: O campo deve estar preenchido");

            }

            @Test
            void validateEmptyName(){

                String name = "";

                assertThatCode(() -> {
                    new Restaurant(name, address);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContainingAll("name: O campo deve estar preenchido", "name: O campo deve ter no minimo 3 e no maximo 100 caracteres");

            }

            @ParameterizedTest
            @ValueSource(strings = {"Ra", "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida"})
            void validateLessMinimumName(String name){

                assertThatCode(() -> {
                    new Restaurant(name, address);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("name: O campo deve ter no minimo 3 e no maximo 100 caracteres");

            }

        }

    }

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
                    faker.address().stateAbbr());

            Restaurant restaurant = new Restaurant(name, address);

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
                    faker.address().stateAbbr());

            Restaurant restaurant = new Restaurant(name, address);

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
            String restaurantId = UUID.randomUUID().toString();
            String name = "Test Restaurant";
            Restaurant.Address address = new Restaurant.Address(
                    faker.address().streetAddress(),
                    Integer.valueOf(faker.address().streetAddressNumber()),
                    faker.address().secondaryAddress(),
                    "Medicina",
                    faker.address().city(),
                    faker.address().stateAbbr());
            LocalDateTime register = LocalDateTime.now();
            List<Restaurant.OpeningHour> openingHours = GenerateData.createDefaultOpeningHours();
            Integer numberOfTables = 5;

            Restaurant restaurant = Restaurant.of(restaurantId, name, address, register, openingHours, numberOfTables, null);

            assertNotNull(restaurant);
            assertEquals(restaurantId, restaurant.getRestaurantId().id());
            assertEquals(name, restaurant.getName());
            assertEquals(address, restaurant.getAddress());
            assertEquals(register, restaurant.getRegister());
            assertEquals(openingHours, restaurant.getOpeningHours());
            assertEquals(numberOfTables, restaurant.getTables());
        }

        @Test
        void createRestaurantWithNullOpeningHours() {
            String restaurantId = UUID.randomUUID().toString();
            String name = "Test Restaurant";
            Restaurant.Address address = new Restaurant.Address(
                    faker.address().streetAddress(),
                    Integer.valueOf(faker.address().streetAddressNumber()),
                    faker.address().secondaryAddress(),
                    "Medicina",
                    faker.address().city(),
                    faker.address().stateAbbr());
            LocalDateTime register = LocalDateTime.now();
            Integer numberOfTables = 5;

            Restaurant restaurant = Restaurant.of(restaurantId, name, address, register, null, numberOfTables, null);

            assertNotNull(restaurant);
            assertEquals(restaurantId, restaurant.getRestaurantId().id());
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

            this.restaurant.changeName(newName);
            assertEquals(newName, this.restaurant.getName());
            assertTrue(this.restaurant.getStateChange());

        }

        @Test
        void changeAddressRestaurant(){

            Restaurant.Address newAddress = GenerateData.generateAddress();

            this.restaurant.changeAddress(newAddress);

            assertEquals(newAddress.getStreet(), this.restaurant.getAddress().getStreet());
            assertEquals(newAddress.getNumber(), this.restaurant.getAddress().getNumber());
            assertEquals(newAddress.getAddittionalDetails(), this.restaurant.getAddress().getAddittionalDetails());
            assertEquals(newAddress.getNeighborhood(), this.restaurant.getAddress().getNeighborhood());
            assertEquals(newAddress.getCity(), this.restaurant.getAddress().getCity());
            assertEquals(newAddress.getState(), this.restaurant.getAddress().getState());

            assertTrue(this.restaurant.getStateChange());


        }

        @Test
        void addOpeningHourRestaurant(){

            Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour(
                    DayOfWeek.FRIDAY,
                    LocalTime.of(10, 0),
                    LocalTime.of(12, 30)
                    );

            var isAdd = this.restaurant.addOpeningHours(openingHour);

            assertTrue(isAdd);
            assertTrue(this.restaurant.getOpeningHours().contains(openingHour));

            assertTrue(this.restaurant.getStateChange());
        }

        @Test
        void addSameOpeningHourRestaurant(){

            Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour(
                    DayOfWeek.FRIDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(18, 0)
            );

            var isNotAdd = this.restaurant.addOpeningHours(openingHour);
            var size = this.restaurant.getOpeningHours().size();

            assertEquals(size, this.restaurant.getOpeningHours().size());
            assertFalse(isNotAdd);

            assertFalse(this.restaurant.getStateChange());


        }

        @Test
        void removeOpeningHourRestaurant(){

            Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour(
                    DayOfWeek.FRIDAY,
                    LocalTime.of(9, 0),
                    LocalTime.of(18, 0)
            );

            var isRemove = this.restaurant.removeOpeningHours(openingHour);

            assertTrue(isRemove);
            assertFalse(this.restaurant.getOpeningHours().contains(openingHour));
            assertTrue(this.restaurant.getStateChange());

        }

        @Test
        void changeNumberOfTablesRestaurant(){

            var numberOfTables = 10;

            this.restaurant.changeNumberOfTables(numberOfTables);

            assertEquals(numberOfTables, this.restaurant.getTables());
            assertTrue(this.restaurant.getStateChange());
        }

        @Test
        void changeNumberOfTablesToZeroRestaurant(){

            var oldNumberOfTables = this.restaurant.getTables();

            var numberOfTables = 0;

            this.restaurant.changeNumberOfTables(numberOfTables);

            assertEquals(oldNumberOfTables, this.restaurant.getTables());
            assertFalse(this.restaurant.getStateChange());
        }

        @Test
        void changeNumberOfTablesToNegativeRestaurant(){

            var oldNumberOfTables = this.restaurant.getTables();

            var numberOfTables = -1;

            this.restaurant.changeNumberOfTables(numberOfTables);

            assertEquals(oldNumberOfTables, this.restaurant.getTables());
            assertFalse(this.restaurant.getStateChange());
        }

        @Test
        void addCuisineRestaurant(){
            var cuisine = Cuisine.AMERICAN;

            var isAdd = this.restaurant.addCuisine(cuisine);

            assertTrue(this.restaurant.getCuisines().contains(cuisine));
            assertTrue(isAdd);
            assertTrue(this.restaurant.getStateChange());
        }

        @Test
        void addTwoSameCuisineRestaurantu(){
            var cuisine = Cuisine.BRAZILIAN;

            var isAdd = this.restaurant.addCuisine(cuisine);

            Restaurant restaurantCopy = Restaurant.of(
                    this.restaurant.getRestaurantId().id(),
                    this.restaurant.getName(),
                    this.restaurant.getAddress(),
                    this.restaurant.getRegister(),
                    this.restaurant.getOpeningHours(),
                    this.restaurant.getTables(),
                    this.restaurant.getCuisines()
            );

            var isNotAdd = restaurantCopy.addCuisine(cuisine);

            assertTrue(isAdd);
            assertTrue(restaurantCopy.getCuisines().contains(cuisine));
            assertEquals(1, restaurantCopy.getCuisines().size());
            assertFalse(isNotAdd);
            assertFalse(restaurantCopy.getStateChange());

        }

        @Test
        void removeCuisineRestaurant(){
            var cuisine = Cuisine.BRAZILIAN;

            var isAdd = this.restaurant.addCuisine(cuisine);

            Restaurant restaurantCopy = Restaurant.of(
                    this.restaurant.getRestaurantId().id(),
                    this.restaurant.getName(),
                    this.restaurant.getAddress(),
                    this.restaurant.getRegister(),
                    this.restaurant.getOpeningHours(),
                    this.restaurant.getTables(),
                    this.restaurant.getCuisines()
            );

            var isRemove = restaurantCopy.removeCuisine(cuisine);

            assertTrue(isAdd);
            assertTrue(isRemove);
            assertTrue(restaurantCopy.getStateChange());
        }



    }



}