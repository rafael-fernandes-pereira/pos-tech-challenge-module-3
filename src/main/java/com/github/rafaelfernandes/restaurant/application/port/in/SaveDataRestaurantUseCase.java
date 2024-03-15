package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

public interface SaveDataRestaurantUseCase {

    Restaurant.RestaurantId create(Restaurant command);



}
