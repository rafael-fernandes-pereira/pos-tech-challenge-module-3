package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.SaveDataRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.application.port.out.CreateRestaurantPort;
import com.github.rafaelfernandes.common.annotations.UseCase;
import lombok.RequiredArgsConstructor;


@UseCase
@RequiredArgsConstructor
public class SaveDataRestaurantService implements SaveDataRestaurantUseCase {

    private final CreateRestaurantPort createRestaurantPort;

    @Override
    public Restaurant.RestaurantId create(Restaurant restaurant) {

        return this.createRestaurantPort.create(restaurant);

    }
}
