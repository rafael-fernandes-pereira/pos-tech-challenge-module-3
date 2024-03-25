package com.github.rafaelfernandes.restaurant.application.port.out;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

public interface ManageRestaurantPort {

    Boolean existsName(String name);

    Restaurant save(Restaurant restaurant);

}
