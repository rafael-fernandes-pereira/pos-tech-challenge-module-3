package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.common.enums.Cuisine;

import java.util.List;
import java.util.Optional;

public interface GetRestaurantUseCase {

    List<Restaurant> findAllBy(String name, String location, List<Cuisine> cuisines);

}
