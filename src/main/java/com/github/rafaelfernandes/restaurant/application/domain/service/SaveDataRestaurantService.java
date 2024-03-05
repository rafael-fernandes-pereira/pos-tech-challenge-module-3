package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.CreateRestaurantCommand;
import com.github.rafaelfernandes.restaurant.application.port.in.SaveDataRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.application.port.out.CreateRestaurantPort;
import com.github.rafaelfernandes.restaurant.common.annotations.UseCase;
import com.github.rafaelfernandes.restaurant.common.enums.State;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class SaveDataRestaurantService implements SaveDataRestaurantUseCase {

    private final CreateRestaurantPort createRestaurantPort;

    @Override
    public Restaurant.RestaurantId create(CreateRestaurantCommand command) {

        var address = new Restaurant.Address(
                command.address().street(),
                command.address().number(),
                command.address().addittionalDetails(),
                command.address().neighborhood(),
                command.address().city(),
                State.valueOf(command.address().state())
        );

        var restaurantNew = Restaurant.create(
                command.name(),
                address
        );

        return this.createRestaurantPort.create(restaurantNew);

    }
}
