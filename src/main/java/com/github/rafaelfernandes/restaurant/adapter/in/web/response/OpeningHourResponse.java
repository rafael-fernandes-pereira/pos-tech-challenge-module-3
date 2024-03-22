package com.github.rafaelfernandes.restaurant.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record OpeningHourResponse(

        @Schema(implementation = DayOfWeek.class)
        String day_of_week,

        LocalTime start,

        LocalTime end



) {
}
