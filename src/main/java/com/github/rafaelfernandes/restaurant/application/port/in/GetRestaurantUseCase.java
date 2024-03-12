package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.util.Optional;

public interface GetRestaurantUseCase {

    Optional<Restaurant> findBy(GetRestarauntDataCommand getRestarauntDataCommand);

}
