package com.github.rafaelfernandes.reservation.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ReservationServiceResponse(

        @Schema(description = "Service Id")
        String id,

        @Schema(description = "Restaurant Id")
        String retaurantId,

        OpeningHourReservationServiceResponse opening_hour,

        LocalDate date

) {


}
