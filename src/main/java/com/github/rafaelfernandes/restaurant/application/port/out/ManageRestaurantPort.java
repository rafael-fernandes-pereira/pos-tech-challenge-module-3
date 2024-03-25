package com.github.rafaelfernandes.restaurant.application.port.out;

import com.github.rafaelfernandes.common.enums.Cuisine;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.util.List;
import java.util.Optional;

public interface ManageRestaurantPort {

    Boolean existsName(String name);

    Restaurant save(Restaurant restaurant);

    Optional<Restaurant> findById(Restaurant.RestaurantId id);

    List<Restaurant> findAllBy(String name, String location, List<Cuisine> cuisines);
}
