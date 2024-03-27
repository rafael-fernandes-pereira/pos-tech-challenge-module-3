package com.github.rafaelfernandes.service.application.domain.model;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mockito;
import util.GenerateData;

class ServiceTest {


    @Nested
    class Validate {

        Restaurant restaurant = GenerateData.createRestaurant();
        Restaurant.OpeningHour openingHour = restaurant.getOpeningHours().get(0);
        LocalDate date = LocalDate.now();
        Integer tables = 10;

        @Test
        void validateSuccess(){

            var service = new Service(restaurant, openingHour, date, tables);

            assertThat(service.getRestaurantId()).isEqualTo(restaurant.getRestaurantId());
            assertThat(service.getOpeningHour()).isEqualTo(openingHour);
            assertThat(service.getDate()).isEqualTo(date);
            assertThat(service.getTables()).isEqualTo(tables);


        }

        @Nested
        class ServiceIdField {

            @ParameterizedTest
            @ValueSource(strings = {"", "invalid-uuid"})
            @NullSource
            void validateNotEmpty(String serviceId){

                assertThatCode(() -> {
                    new Service.ServiceId(serviceId);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContaining("id: O campo deve ser do tipo UUID");
            }

            @Test
            void validateSuccess(){

                var serviceId = new Service.ServiceId("94a08269-d6cd-4811-8198-b296eff841a8");
                assertThat(serviceId.id()).isEqualTo("94a08269-d6cd-4811-8198-b296eff841a8");

            }



        }

        @Nested
        class RestaurantField {

            @ParameterizedTest
            @NullSource
            void validateNotEmpty(Restaurant restaurant){

                assertThatCode(() -> {
                    new Service(restaurant, openingHour, date, tables);
                })
                        .isInstanceOf(ValidationException.class)
                        .hasMessageContaining("restaurant: O campo deve estar preenchido");
            }

        }

        @Nested
        class OpeningHourField {

            @ParameterizedTest
            @NullSource
            void validateNotEmpty(Restaurant.OpeningHour openingHour){

                assertThatCode(() -> {
                    new Service(restaurant, openingHour, date, tables);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContaining("openingHour: O campo deve estar preenchido");
            }
        }

        @Nested
        class DateField {

            @ParameterizedTest
            @NullSource
            void validateNotEmpty(LocalDate date){

                assertThatCode(() -> {
                    new Service(restaurant, openingHour, date, tables);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContaining("A data do serviço dever ser hoje ou futura");
            }

            @Test
            void validateDayPast(){

                var date = LocalDate.now().minusDays(1);

                assertThatCode(() -> {
                    new Service(restaurant, openingHour, date, tables);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContaining("A data do serviço dever ser hoje ou futura");
            }
        }

        @Nested
        class TablesField {

            @ParameterizedTest
            @NullSource
            @ValueSource(ints = {-1, 0})
            void validateNotEmpty(Integer tables){

                assertThatCode(() -> {
                    new Service(restaurant, openingHour, date, tables);
                })
                        .isInstanceOf(ConstraintViolationException.class)
                        .hasMessageContaining("tables: O mínimo de mesas é 1");
            }
        }

        @Nested
        class Rules {

            String name = "A Oca";
            Restaurant.Address address = GenerateData.generateAddress();
            List<Restaurant.Cuisine> cuisines = GenerateData.generateCuisines();

            List<Restaurant.OpeningHour> openingHours = GenerateData.createDefaultOpeningHours();
            Restaurant.OpeningHour openingHourService = openingHours.get(0);

            @Nested
            class Tables {

                @Test
                void validateTablesMoreThanRestaurant(){

                    // Arrange

                    var restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

                    var tablesService = 11;

                    // Act && Assert

                    assertThatCode(() -> {
                        new Service(restaurant, openingHourService, date, tablesService);
                    })
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining("tables: o número de mesas para o serviço é maior que a cadastrada no restaurante");

                }

                @Test
                void validateSuccess(){

                    // Arrange

                    var restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

                    var tablesService = 10;

                    // Act

                    var service = new Service(restaurant, openingHourService, date, tablesService);

                    // Assert

                    assertThat(service).isNotNull();

                }

            }

            @Nested
            class OpeningHour {



                @Test
                void validateOpeningHourNotContainsDayInRestaurant(){

                    // Arrange

                    var openingHours = new ArrayList<Restaurant.OpeningHour>(){{
                        add(
                                new Restaurant.OpeningHour(
                                    DayOfWeek.MONDAY.name(),
                                    LocalTime.of(8, 0),
                                    LocalTime.of(18, 0)
                                )
                        );
                    }};

                    var restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

                    var openingHourService = new Restaurant.OpeningHour(
                            DayOfWeek.TUESDAY.name(),
                            LocalTime.of(8, 0),
                            LocalTime.of(18, 0)
                    );

                    //

                    assertThatCode(() -> {
                        new Service(restaurant, openingHourService, date, tables);
                    })
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining("openingHour: não existe esse horário no restaurante enviado.");

                }

                @Test
                void validateOpeningHourNotContainsStartInRestaurant(){

                    // Arrange

                    var openingHours = new ArrayList<Restaurant.OpeningHour>(){{
                        add(
                                new Restaurant.OpeningHour(
                                        DayOfWeek.MONDAY.name(),
                                        LocalTime.of(8, 0),
                                        LocalTime.of(18, 0)
                                )
                        );
                    }};

                    var restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

                    var openingHourService = new Restaurant.OpeningHour(
                            DayOfWeek.MONDAY.name(),
                            LocalTime.of(7, 0),
                            LocalTime.of(18, 0)
                    );

                    //

                    assertThatCode(() -> {
                        new Service(restaurant, openingHourService, date, tables);
                    })
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining("openingHour: não existe esse horário no restaurante enviado.");

                }

                @Test
                void validateOpeningHourNotContainsEndInRestaurant(){

                    // Arrange

                    var openingHours = new ArrayList<Restaurant.OpeningHour>(){{
                        add(
                                new Restaurant.OpeningHour(
                                        DayOfWeek.MONDAY.name(),
                                        LocalTime.of(8, 0),
                                        LocalTime.of(18, 0)
                                )
                        );
                    }};

                    var restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

                    var openingHourService = new Restaurant.OpeningHour(
                            DayOfWeek.MONDAY.name(),
                            LocalTime.of(8, 0),
                            LocalTime.of(19, 0)
                    );

                    //

                    assertThatCode(() -> {
                        new Service(restaurant, openingHourService, date, tables);
                    })
                            .isInstanceOf(IllegalArgumentException.class)
                            .hasMessageContaining("openingHour: não existe esse horário no restaurante enviado.");

                }

                @Test
                void validateOpeningHourSuccess(){

                    // Arrange

                    var openingHours = new ArrayList<Restaurant.OpeningHour>(){{
                        add(
                                new Restaurant.OpeningHour(
                                        DayOfWeek.MONDAY.name(),
                                        LocalTime.of(8, 0),
                                        LocalTime.of(18, 0)
                                )
                        );
                    }};

                    var restaurant = new Restaurant(name, address, openingHours, cuisines, 10);

                    var openingHourService = new Restaurant.OpeningHour(
                            DayOfWeek.MONDAY.name(),
                            LocalTime.of(9, 0),
                            LocalTime.of(12, 0)
                    );

                    // Act

                    var service = new Service(restaurant, openingHourService, date, tables);

                    // Assert

                    assertThat(service).isNotNull();

                }



            }

        }

    }

}