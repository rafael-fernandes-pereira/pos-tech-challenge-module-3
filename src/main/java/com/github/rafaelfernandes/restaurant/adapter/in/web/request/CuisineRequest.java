package com.github.rafaelfernandes.restaurant.adapter.in.web.request;

import com.github.rafaelfernandes.restaurant.common.enums.Cuisine;
import io.swagger.v3.oas.annotations.media.Schema;

public record CuisineRequest(

        @Schema(implementation = Cuisine.class)
        String cuisine
) {
}
