package com.github.rafaelfernandes.service.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record OpeningHourServiceRequest(

        @Schema(implementation = DayOfWeek.class)
        String day_of_week,

        LocalTime start,

        LocalTime end



) {
}
