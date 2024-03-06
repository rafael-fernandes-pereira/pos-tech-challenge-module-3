package com.github.rafaelfernandes.restaurant.application.port.in;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import static com.github.rafaelfernandes.restaurant.common.validation.Validation.validate;

public record CreateRestaurantCommand(
        @NotEmpty(message = "O campo deve estar preenchido")
        @Length(min = 3, max = 100, message = "O campo deve ter no minimo 3 e no maximo 100 caracteres")
        String name,
        @NotNull(message = "O campo deve estar preenchido")
        CreateRestaurantAddressCommand address

) {

        public CreateRestaurantCommand(String name, CreateRestaurantAddressCommand address) {
                this.name = name;
                this.address = address;
                validate(this);
        }
}



