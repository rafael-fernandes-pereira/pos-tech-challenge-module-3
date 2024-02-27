package com.github.rafaelfernandes.restaurant.application.model;

import com.github.rafaelfernandes.restaurant.common.Cuisine;
import com.github.rafaelfernandes.restaurant.common.State;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Restaurant {

    @Getter private final RestaurantId restaurantId;

    @Getter private final String name;

    @Getter private final Address address;

    @Getter private final LocalDateTime register;

    @Getter private final List<OpeningHour> openingHours;

    @Getter private final List<Table> tables;

    @Getter private final List<Cuisine> cuisines;

    @Value
    public static class RestaurantId {
        UUID value;
    }

    @Value
    public static class Address{
        String publicArea;
        Integer number;
        String addittionalDetails;
        String neighborhood;
        String city;
        State state;

    }

    @Value
    public static class OpeningHour {
        DayOfWeek dayOfWeek;
        LocalDateTime start;
        LocalDateTime end;
    }

    @Value
    public static class Table {
        public static final Integer NUMBER_OF_SEATS = 4;
    }

    public static Restaurant create(String name, Address address, Integer numberOfTables){

        RestaurantId restaurantId = new RestaurantId(UUID.randomUUID());

        LocalDateTime register = LocalDateTime.now();

        ArrayList<Table> tables = getTables(numberOfTables);

        List<OpeningHour> openingHours = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LocalDateTime start = LocalDateTime.of(2024, 1, 1, 9, 0); // Exemplo: 9h
            LocalDateTime end = LocalDateTime.of(2024, 1, 1, 18, 0);  // Exemplo: 18h
            openingHours.add(new OpeningHour(dayOfWeek, start, end));
        }

        return new Restaurant(restaurantId, name, address, register, openingHours, tables, null);
    }

    private static ArrayList<Table> getTables(Integer numberOfTables) {
        ArrayList<Table> tables = new ArrayList<>(numberOfTables);

        for (int i = 0; i < numberOfTables; i++) {
            tables.add(new Table());
        }
        return tables;
    }

    public static Restaurant of(UUID restaurantId, String name, Address address, LocalDateTime register, List<OpeningHour> openingHours, Integer numberOfTables, List<Cuisine> cuisines){
        return new Restaurant(new RestaurantId(restaurantId), name, address, register, openingHours, getTables(numberOfTables), cuisines);
    }




}
