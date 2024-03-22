package com.github.rafaelfernandes.restaurant.adapter.in.web.response;

import com.github.rafaelfernandes.restaurant.adapter.in.web.request.CuisineRequest;
import com.github.rafaelfernandes.restaurant.adapter.in.web.request.OpeningHourRequest;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.UUID;

@Schema(name = "RestaurantResponse", description = "Data of restaurant")
public record RestaurantResponse(

        @Schema(description = "Id of restaurant")
        UUID id,

        @Schema(description = "Name of restaurant")
        String name,

        @Schema(name = "address", description = "address of restaurant")
        AddressResponse address,

        @Schema(name = "tables", description = "quantity of tables in restaurant")
        Integer tables,

        @Schema(name = "opening_hour", description = "opening hours of restaurant")
        List<OpeningHourResponse> opening_hour,

        @Schema(name = "cuisines", description = "type of cuines in restaurant")
        List<CuisineResponse> cuisines

) {
}
