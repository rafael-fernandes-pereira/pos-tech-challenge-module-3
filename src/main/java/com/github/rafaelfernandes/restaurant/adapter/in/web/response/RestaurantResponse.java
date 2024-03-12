package com.github.rafaelfernandes.restaurant.adapter.in.web.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(name = "Restaurant", description = "Data of restaurant")
public record RestaurantResponse(

        @Schema(description = "Id of restaurant")
        UUID id,

        @Schema(description = "Name of restaurant")
        String name,

        @Schema(name = "address", description = "address of restaurant")
        AddressResponse address) {
}
