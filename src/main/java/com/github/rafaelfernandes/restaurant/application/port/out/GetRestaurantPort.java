package com.github.rafaelfernandes.restaurant.application.port.out;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.common.enums.Cuisine;
import com.github.rafaelfernandes.restaurant.common.enums.OrderBy;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GetRestaurantPort {

    Optional<Restaurant> findById(UUID id);

    List<Restaurant> findAllBy(String name,
                               String location,
                               List<Cuisine> cuisines,
                               Integer page,
                               Integer quantity,
                               OrderBy orderBy);

}
