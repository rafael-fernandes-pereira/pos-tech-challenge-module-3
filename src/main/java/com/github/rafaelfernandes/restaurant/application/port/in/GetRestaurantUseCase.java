package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.common.enums.Cuisine;
import com.github.rafaelfernandes.restaurant.common.enums.OrderBy;

import java.util.List;
import java.util.Optional;

public interface GetRestaurantUseCase {

    Optional<Restaurant> findById(Restaurant.RestaurantId restaurantId);

    List<Restaurant> findAllBy(String name, String location, List<Cuisine> cuisines, Integer page, Integer quantity, OrderBy orderBy);

}
