package com.github.rafaelfernandes.restaurant.application.port.out;

import com.github.rafaelfernandes.restaurant.domain.Restaurant;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetRestaurantPort {

    Optional<Restaurant> findById(UUID id);

    List<Restaurant> findAllBy(String name,
                               String location,
                               List<String> cuisines,
                               Integer page,
                               Integer quantity,
                               String orderBy);

}
