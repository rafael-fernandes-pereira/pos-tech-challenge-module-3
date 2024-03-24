package com.github.rafaelfernandes.service.adapter.in.web.response;

import com.github.rafaelfernandes.service.adapter.in.web.request.OpeningHourServiceRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;

public record ServiceResponse(

        @Schema(description = "Service Id")
        String id,

        @Schema(description = "Restaurant Id")
        String retaurantId,

        OpeningHourServiceResponse opening_hour,

        LocalDate date,

        @Schema(description = "Quantity of available tables ")
        Integer tables

) {


}
