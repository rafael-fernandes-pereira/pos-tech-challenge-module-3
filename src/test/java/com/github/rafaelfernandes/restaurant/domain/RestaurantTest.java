package com.github.rafaelfernandes.restaurant.domain;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import util.GenerateData;
import net.datafaker.Faker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantTest {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));

    String name = "A Oca";

    Restaurant.Address address =  GenerateData.generateAddress();

    List<Restaurant.OpeningHour> openingHours = GenerateData.createDefaultOpeningHours();

    List<Restaurant.Cuisine> cuisines = GenerateData.generateCuisines();

    @Nested
    class Validate {

        @Nested
        class Name {


            @ParameterizedTest
            @ValueSource(strings = {"Rafael Fernandes Pereira", "Dio", "Gabriela Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira"})
            void validateSucessName(String name){

                var restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

                assertEquals(name, restaurant.getName());

            }

            @Test
            void validateNullName(){

                assertThatCode(() -> {
                    new Restaurant(null, address, openingHours, cuisines, 10);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("name: O campo deve estar preenchido");

            }

            @Test
            void validateEmptyName(){

                String name = "";

                assertThatCode(() -> {
                    new Restaurant(name, address, openingHours, cuisines, 10);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContainingAll("name: O campo deve estar preenchido", "name: O campo deve ter no minimo 3 e no maximo 100 caracteres");

            }

            @ParameterizedTest
            @ValueSource(strings = {"Ra", "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida"})
            void validateLengthMinMaxName(String name){

                assertThatCode(() -> {
                    new Restaurant(name, address, openingHours, cuisines, 10);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("name: O campo deve ter no minimo 3 e no maximo 100 caracteres");

            }

        }

        @Nested
        class Tables {

            @ParameterizedTest
            @ValueSource(ints = {-1, 0})
            @NullSource
            void validateNonPositiveTables(Integer tables){
                assertThatCode(() -> {
                    new Restaurant(name, address, openingHours, cuisines, tables);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessage("tables: O campo deve ser maior que zero (0)");
            }

            @Test
            void validateSuccessTables(){
                var restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

                assertThat(restaurant.getTables()).isEqualTo(10);
            }

        }

        @Nested
        class Address {

            String street = "Avenida Paraíso";
            Integer number = 10;
            String addittionalDetails = "Ao Lado da Lanchonete";
            String neighborhood = "CT Araucária";
            String city = "Ponta Grossa";
            String state = "MG";

            @Nested
            class Street {

                @ParameterizedTest
                @ValueSource(strings = {"","Ra", "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida"})
                void validateMinMaxEmptyStreet(String street){
                    assertThatCode(() -> {
                        new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);
                    })
                            .isInstanceOf(ConstraintViolationException.class)
                            .hasMessageContaining("street: O campo deve ter no minimo 10 e no maximo 150 caracteres");
                }

                @ParameterizedTest
                @NullSource
                void validateNullStreet(String street){
                    assertThatCode(() -> {
                        new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);
                    })
                            .isInstanceOf(ConstraintViolationException.class)
                            .hasMessageContaining("street: O campo deve estar preenchido");
                }

                @ParameterizedTest
                @ValueSource(strings = {"Rafael Fernandes Pereira", "DioMioLioA", "Gabriela Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira"})
                void validateSuccessStreet(String street) {
                    var address = new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);

                    assertThat(address.getStreet()).isEqualTo(street);

                }

            }

            @Nested
            class Number {

                @ParameterizedTest
                @ValueSource(ints = {-1, 0})
                @NullSource
                void validateNonPositiveTables(Integer number){
                    assertThatCode(() -> {
                        new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);
                    })
                            .isInstanceOf(ConstraintViolationException.class)
                            .hasMessage("number: O campo deve ser maior que zero (0)");
                }

                @Test
                void validateSuccessNumber(){
                    var address = new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);

                    assertThat(address.getNumber()).isEqualTo(number);
                }

            }

            @Nested
            class AddittionalDetails {

                @ParameterizedTest
                @ValueSource(strings = {"Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida"})
                void validateMinMaxEmptyAddittionalDetails(String addittionalDetails){
                    assertThatCode(() -> {
                        new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);
                    })
                            .isInstanceOf(ConstraintViolationException.class)
                            .hasMessageContaining("addittionalDetails: O campo deve ter no máximo 150 caracteres")
                    ;
                }

                @ParameterizedTest
                @ValueSource(strings = {"", "Opa Opa", "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira M"})
                @NullSource
                void validateSuccessAddittionalDetails(String addittionalDetails){
                    var address = new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);

                    assertThat(address.getAddittionalDetails()).isEqualTo(addittionalDetails);
                }

            }

            @Nested
            class Neighborhood {

                @ParameterizedTest
                @ValueSource(strings = {"", "Ra", "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida"})
                void validateMinMaxEmptyStreet(String neighborhood){
                    assertThatCode(() -> {
                        new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);
                    })
                            .isInstanceOf(ConstraintViolationException.class)
                            .hasMessageContaining("neighborhood: O campo deve ter no minimo 3 e no máximo 30 caracteres");
                }

                @ParameterizedTest
                @NullSource
                void validateNullStreet(String neighborhood){
                    assertThatCode(() -> {
                        new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);
                    })
                            .isInstanceOf(ConstraintViolationException.class)
                            .hasMessageContaining("neighborhood: O campo deve estar preenchido");
                }

                @ParameterizedTest
                @ValueSource(strings = {"Rafael Fernandes Pereira", "Dio", "Gabriela Carolina da Silva San"})
                void validateSuccessStreet(String neighborhood) {
                    var address = new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);

                    assertThat(address.getNeighborhood()).isEqualTo(neighborhood);

                }


            }

            @Nested
            class City {

                @ParameterizedTest
                @ValueSource(strings = {"", "Ra", "Esmeralda Carolina da Silva Santos Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida Costa Ferreira Gomes Souza Lima Freitas Lima Pereira Oliveira Martins Rodrigues Barbosa Almeida"})
                void validateMinMaxEmptyCity(String city){
                    assertThatCode(() -> {
                        new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);
                    })
                            .isInstanceOf(ConstraintViolationException.class)
                            .hasMessageContaining("city: O campo deve ter no minimo 3 e no máximo 60 caracteres");
                }

                @ParameterizedTest
                @NullSource
                void validateNullCity(String city){
                    assertThatCode(() -> {
                        new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);
                    })
                            .isInstanceOf(ConstraintViolationException.class)
                            .hasMessageContaining("city: O campo deve estar preenchido");
                }

                @ParameterizedTest
                @ValueSource(strings = {"Rafael Fernandes Pereira", "Dio", "Gabriela Carolina da Silva SanGabriela Carolina da Silva San"})
                void validateSuccessCity(String city) {
                    var address = new Restaurant.Address(street, number, addittionalDetails, neighborhood, city, state);

                    assertThat(address.getCity()).isEqualTo(city);

                }


            }

            @Nested
            class State {



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

            Restaurant restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

            assertNotNull(restaurant);
            assertNotNull(restaurant.getRestaurantId());
            assertEquals(name, restaurant.getName());
            assertEquals(address, restaurant.getAddress());
            assertNotNull(restaurant.getRegister());
            assertNotNull(restaurant.getOpeningHours());
            assertEquals(10, restaurant.getTables());
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

            Restaurant restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

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





}
