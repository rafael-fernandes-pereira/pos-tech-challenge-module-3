package com.github.rafaelfernandes.service.application.port.out;

import com.github.rafaelfernandes.service.application.domain.model.Service;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.time.LocalDate;

public interface ManageServicePort {

    Boolean existsService(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date);

    Service save(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables);

    Service details(Service.ReservationId reservationId);

}
