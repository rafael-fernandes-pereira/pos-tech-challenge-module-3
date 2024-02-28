package com.github.rafaelfernandes.restaurant.application.model;

import com.github.rafaelfernandes.restaurant.common.Cuisine;
import com.github.rafaelfernandes.restaurant.common.State;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Restaurant {

    private final RestaurantId restaurantId;

    private final String name;

    private final Address address;

    private final LocalDateTime register;

    private final List<OpeningHour> openingHours;

    private final Integer tables;

    private final List<Cuisine> cuisines;

    @Value
    public static class RestaurantId {
        UUID value;
    }

    @Value
    public static class Address{
        String street;
        Integer number;
        String addittionalDetails;
        String neighborhood;
        String city;
        State state;

    }

    @Value
    public static class OpeningHour {
        DayOfWeek dayOfWeek;
        LocalTime start;
        LocalTime end;
    }

    public static Restaurant create(String name, Address address){

        RestaurantId restaurantId = new RestaurantId(UUID.randomUUID());

        LocalDateTime register = LocalDateTime.now();

        List<OpeningHour> openingHours = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LocalTime start = LocalTime.of(9, 0);
            LocalTime end = LocalTime.of(18, 0);
            openingHours.add(new OpeningHour(dayOfWeek, start, end));
        }

        return new Restaurant(restaurantId, name, address, register, openingHours, 0, null);
    }

    public static Restaurant of(UUID restaurantId, String name, Address address, LocalDateTime register, List<OpeningHour> openingHours, Integer numberOfTables, List<Cuisine> cuisines){
        return new Restaurant(new RestaurantId(restaurantId), name, address, register, openingHours, numberOfTables, cuisines);
    }

    public Restaurant changeName(String name){
        return new Restaurant(this.restaurantId,
                name,
                this.address,
                this.register,
                this.openingHours,
                this.tables,
                this.cuisines
        );
    }

    public Restaurant changeAddress(Address address){
        return new Restaurant(this.restaurantId,
                this.name,
                address,
                this.register,
                this.openingHours,
                this.tables,
                this.cuisines
        );
    }

    public Restaurant addOpeningHours(OpeningHour openingHour){
        List<OpeningHour> openingHoursNew = this.getOpeningHours();
        openingHoursNew.add(openingHour);

        return new Restaurant(this.restaurantId,
                this.name,
                this.address,
                this.register,
                openingHoursNew,
                this.tables,
                this.cuisines
        );
    }

    public Restaurant removeOpeningHours(OpeningHour openingHour){
        List<OpeningHour> openingHoursNew = this.getOpeningHours();
        openingHoursNew.remove(openingHour);

        return new Restaurant(this.restaurantId,
                this.name,
                this.address,
                this.register,
                openingHoursNew,
                this.tables,
                this.cuisines
        );
    }


    public Restaurant changeNumberOfTables(Integer numberOfTables){
        return new Restaurant(this.restaurantId,
                this.name,
                this.address,
                this.register,
                this.openingHours,
                numberOfTables,
                this.cuisines
        );
    }






}
