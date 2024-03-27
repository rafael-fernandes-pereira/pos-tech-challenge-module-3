package com.github.rafaelfernandes.service.application.port.out;

import com.github.rafaelfernandes.service.application.domain.model.Service;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.time.LocalDate;

public interface ManageServicePort {

    Boolean existsService(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date);

    Service save(Service service);

    Service details(Service.ServiceId reservationId);

}
