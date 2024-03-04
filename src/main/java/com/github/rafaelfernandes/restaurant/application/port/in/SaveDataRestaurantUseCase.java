package com.github.rafaelfernandes.restaurant.application.port.in;

import java.util.UUID;

public interface SaveDataRestaurantUseCase {

    UUID create(CreateRestaurantCommand command);



}
