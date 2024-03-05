package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.restaurant.common.enums.State;
import com.github.rafaelfernandes.restaurant.common.validation.ValueOfEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

import static com.github.rafaelfernandes.restaurant.common.validation.Validation.validate;

public record CreateRestaurantAddressCommand (
        @NotEmpty(message = "O campo deve estar preenchido")
        @Length( min = 10, max = 150, message = "O campo deve ter no minimo 10 e no maximo 150 caracteres")
        String street,

        @NotNull(message = "O campo deve estar preenchido")
        @Positive(message = "O campo deve ser maior que zero (0)")
        Integer number,

        @Length( max = 150, message = "O campo deve ter no máximo 150 caracteres")
        String addittionalDetails,

        @NotEmpty(message = "O campo deve estar preenchido")
        @Length( min = 3, max = 30, message = "O campo deve ter no minimo 3 e no máximo 30 caracteres")
        String neighborhood,

        @NotEmpty(message = "O campo deve estar preenchido")
        @Length( min = 3, max = 60, message = "O campo deve ter no minimo 3 e no máximo 60 caracteres")
        String city,

        @NotNull(message = "O campo deve estar preenchido")
        @ValueOfEnum(enumClass = State.class, message = "O campo deve ser uma sigla de estado válida")
        String state

){

        public CreateRestaurantAddressCommand(String street, Integer number, String addittionalDetails, String neighborhood, String city, String state) {
                this.street = street;
                this.number = number;
                this.addittionalDetails = addittionalDetails;
                this.neighborhood = neighborhood;
                this.city = city;
                this.state = state;
                validate(this);
        }
}



