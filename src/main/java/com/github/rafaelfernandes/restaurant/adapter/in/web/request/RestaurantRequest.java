package com.github.rafaelfernandes.restaurant.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(name = "Restaurant", description = "Data of restaurant")
public record RestaurantRequest(

        @Schema(description = "Name of restaurant")
        String name,

        @Schema(name = "address", description = "address of restaurant")
        AddressRequest address,

        @Schema(name = "tables", description = "quantity of tables in restaurant")
        Integer tables,

        @Schema(name = "opening_hour", description = "opening hours of restaurant")
        List<OpeningHourRequest> opening_hour,

        @Schema(name = "cuisines", description = "type of cuines in restaurant")
        List<CuisineRequest> cuisines


) {
}
