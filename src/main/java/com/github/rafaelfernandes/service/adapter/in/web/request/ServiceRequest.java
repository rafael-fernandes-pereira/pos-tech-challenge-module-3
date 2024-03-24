package com.github.rafaelfernandes.service.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ServiceRequest(

        @Schema(description = "Restaurant Id")
        String retaurantId,

        OpeningHourServiceRequest opening_hour,

        LocalDate date,

        @Schema(description = "Quantity of available tables ")
        Integer tables

) {
}
