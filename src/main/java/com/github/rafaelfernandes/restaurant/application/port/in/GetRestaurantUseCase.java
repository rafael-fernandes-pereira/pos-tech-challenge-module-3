package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.restaurant.domain.Restaurant;

import java.util.List;
import java.util.Optional;

public interface GetRestaurantUseCase {

    Optional<Restaurant> findBy(GetRestarauntDataCommand getRestarauntDataCommand);

    List<Restaurant> findAlldBy(GetRestarauntDataCommand getRestarauntDataCommand);

}
