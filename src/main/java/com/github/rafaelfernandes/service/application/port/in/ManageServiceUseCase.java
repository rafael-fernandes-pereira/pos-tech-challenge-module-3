package com.github.rafaelfernandes.service.application.port.in;

import com.github.rafaelfernandes.service.application.domain.model.Service;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.time.LocalDate;

public interface ManageServiceUseCase {

    Service.ServiceId create(Restaurant.RestaurantId restaurantid, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables);

    Service details(Service.ServiceId reservationId);

    Boolean update(Service reservation);


}
