package com.github.rafaelfernandes.restaurant.application.port.out;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.util.Optional;

public interface ManageRestaurantPort {

    Boolean existsName(String name);

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Restaurant.RestaurantId id);
}
