package com.github.rafaelfernandes.reservation.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

public record ReservationRequest(
        @Schema(description = "Service Id", implementation = UUID.class)
        String serviceId,

        @Schema(description = "Quantity of tables")
        Integer tables,

        @Schema(description = "Client id", implementation = UUID.class)
        String clientId,

        @Schema(description = "Email of Client")
        String email
) {
}
