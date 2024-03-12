package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.restaurant.common.validation.Validation;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.UUID;

public record GetRestarauntDataCommand(
        @UUID(message = "O campo deve ser do tipo UUID")
        String restaurantId
) {

    public GetRestarauntDataCommand(String restaurantId){
        this.restaurantId = restaurantId;
        Validation.validate(this);
    }

}
