package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.restaurant.domain.Restaurant;

public interface SaveDataRestaurantUseCase {

    Restaurant.RestaurantId create(Restaurant command);



}
