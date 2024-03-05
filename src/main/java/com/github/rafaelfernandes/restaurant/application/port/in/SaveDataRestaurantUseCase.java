package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.util.UUID;

public interface SaveDataRestaurantUseCase {

    Restaurant.RestaurantId create(CreateRestaurantCommand command);



}
