package com.github.rafaelfernandes.reservation.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

public record ReservationRequest(
        @Schema(description = "Service Id")
        String serviceId,

        @Schema(description = "Quantity of tables")
        Integer tables,

        @Schema(description = "Client id")
        String clientId,

        @Schema(description = "Email of Client")
        String email
) {
}
