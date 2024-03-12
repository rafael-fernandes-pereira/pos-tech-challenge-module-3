package com.github.rafaelfernandes.restaurant.application.port.out;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.util.Optional;
import java.util.UUID;

public interface GetRestaurantPort {

    Optional<Restaurant> findById(UUID id);

}
