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

    private String name;

    private Address address;

    private final LocalDateTime register;

    private List<OpeningHour> openingHours;

    private Integer tables;

    private List<Cuisine> cuisines;

    private Boolean stateChange;

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

        var restaurantId = new RestaurantId(UUID.randomUUID());

        var register = LocalDateTime.now();

        var openingHours = new ArrayList<OpeningHour>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LocalTime start = LocalTime.of(9, 0);
            LocalTime end = LocalTime.of(18, 0);
            openingHours.add(new OpeningHour(dayOfWeek, start, end));
        }

        return new Restaurant(restaurantId, name, address, register, openingHours, 0, new ArrayList<Cuisine>(), Boolean.FALSE);
    }

    public static Restaurant of(UUID restaurantId, String name, Address address, LocalDateTime register, List<OpeningHour> openingHours, Integer numberOfTables, List<Cuisine> cuisines){
        return new Restaurant(new RestaurantId(restaurantId), name, address, register, openingHours, numberOfTables, cuisines, Boolean.FALSE);
    }

    public void changeName(String name){
        this.stateChange = Boolean.TRUE;
        this.name = name;
    }

    public void changeAddress(Address address){
        this.stateChange = Boolean.TRUE;
        this.address = address;
    }

    public Boolean addOpeningHours(OpeningHour openingHour){
        if (this.openingHours.contains(openingHour))
            return Boolean.FALSE;

        this.openingHours.add(openingHour);
        this.stateChange = Boolean.TRUE;

        return Boolean.TRUE;
    }

    public Boolean removeOpeningHours(OpeningHour openingHour){
        if (this.openingHours.remove(openingHour)){
            this.stateChange = Boolean.TRUE;
            return Boolean.TRUE;
        }

        return Boolean.FALSE;
    }

    public void changeNumberOfTables(Integer numberOfTables){
        if (numberOfTables > 0){
            this.stateChange = Boolean.TRUE;
            this.tables = numberOfTables;
        }
    }

    public Boolean addCuisine(Cuisine cuisine){
        if (this.cuisines.contains(cuisine)){
            return Boolean.FALSE;
        }

        this.cuisines.add(cuisine);
        this.stateChange = Boolean.TRUE;

        return Boolean.TRUE;
    }

    public Boolean removeCuisine(Cuisine cuisine){

        if(this.cuisines.remove(cuisine)){
            this.stateChange = Boolean.TRUE;
            return Boolean.TRUE;
        }
        return Boolean.FALSE;

    }






}
