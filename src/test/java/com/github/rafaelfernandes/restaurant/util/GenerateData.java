package com.github.rafaelfernandes.restaurant.util;

import com.github.rafaelfernandes.restaurant.application.model.Restaurant;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GenerateData {

    public static List<Restaurant.OpeningHour> createDefaultOpeningHours(){
        List<Restaurant.OpeningHour> openingHours = new ArrayList<>();
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            LocalDateTime start = LocalDateTime.of(2024, 1, 1, 9, 0); // Exemplo: 9h
            LocalDateTime end = LocalDateTime.of(2024, 1, 1, 18, 0);  // Exemplo: 18h
            openingHours.add(new Restaurant.OpeningHour(dayOfWeek, start, end));
        }

        return openingHours;
    }

}
