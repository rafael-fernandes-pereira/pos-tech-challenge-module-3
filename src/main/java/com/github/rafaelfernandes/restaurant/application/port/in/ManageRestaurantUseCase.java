package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.common.enums.Cuisine;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface ManageRestaurantUseCase {

    Restaurant.RestaurantId create(Restaurant command);

    Restaurant findById(Restaurant.RestaurantId restaurantId);

    List<Restaurant> findAllBy(String name, String location, List<Cuisine> cuisines);


}
