package com.github.rafaelfernandes.restaurant.adapter.in.web.response;

import com.github.rafaelfernandes.restaurant.common.enums.Cuisine;
import io.swagger.v3.oas.annotations.media.Schema;

public record CuisineResponse(

        @Schema(implementation = Cuisine.class)
        String cuisine
) {
}
