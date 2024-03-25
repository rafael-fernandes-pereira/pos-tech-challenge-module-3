package com.github.rafaelfernandes.reservation.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ReservationResponse(

        @Schema(description = "Reservation Id")
        String reservationId,

        @Schema(description = "Code of reservation")
        String code,

        @Schema(description = "Data of Service")
        ReservationServiceResponse service,

        @Schema(description = "Quantity of tables")
        Integer tables,

        @Schema(description = "Client id")
        String clientId,

        @Schema(description = "Email of Client")
        String email
) {
}
