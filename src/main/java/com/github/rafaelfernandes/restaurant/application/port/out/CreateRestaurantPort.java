package com.github.rafaelfernandes.restaurant.application.port.out;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantDuplicateException;

public interface CreateRestaurantPort {

    Restaurant.RestaurantId create(Restaurant restaurant) throws RestaurantDuplicateException;

}
