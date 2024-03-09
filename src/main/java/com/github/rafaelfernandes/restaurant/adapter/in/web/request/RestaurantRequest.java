package com.github.rafaelfernandes.restaurant.adapter.in.web.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "Restaurant", description = "Data of restaurant")
public record RestaurantRequest(

        @Schema(description = "Name of restaurant")
        String name,

        @Schema(name = "address", description = "address of restaurant")
        AddressRequest address) {
}
