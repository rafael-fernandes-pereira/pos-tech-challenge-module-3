package com.github.rafaelfernandes.restaurant.util;

import com.github.rafaelfernandes.restaurant.application.model.Restaurant;
import com.github.rafaelfernandes.restaurant.common.State;
import net.datafaker.Faker;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class GenerateData {

    private static final Faker faker = new Faker(new Locale("pt", "BR"));

    private static final Random random = new Random();

    public static List<Restaurant.OpeningHour> createDefaultOpeningHours(){
        List<Restaurant.OpeningHour> openingHours = new ArrayList<>();

        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LocalTime start = LocalTime.of(9, 0);
            LocalTime end = LocalTime.of(18, 0);
            openingHours.add(new Restaurant.OpeningHour(dayOfWeek, start, end));
        }

        return openingHours;
    }

    public static Restaurant createRestaurant(){
        String name = faker.restaurant().name();
        Restaurant.Address address = generateAddress();

        return Restaurant.create(name, address);
    }

    public static Restaurant.Address generateAddress() {
        return new Restaurant.Address(
                faker.address().streetAddress(),
                Integer.valueOf(faker.address().streetAddressNumber()),
                faker.address().secondaryAddress(),
                faker.name().lastName(),
                faker.address().city(),
                State.valueOf(faker.address().stateAbbr()));
    }


}
