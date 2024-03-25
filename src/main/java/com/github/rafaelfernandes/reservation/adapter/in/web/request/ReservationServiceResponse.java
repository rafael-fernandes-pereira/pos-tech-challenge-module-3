package com.github.rafaelfernandes.reservation.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

public record ReservationServiceResponse(

        @Schema(description = "Service Id", implementation = UUID.class)
        String id,

        @Schema(description = "Restaurant Id", implementation = UUID.class)
        String retaurantId,

        OpeningHourReservationServiceResponse opening_hour,

        LocalDate date

) {


}
