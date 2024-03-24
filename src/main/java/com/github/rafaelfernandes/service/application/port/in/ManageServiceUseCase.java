package com.github.rafaelfernandes.service.application.port.in;

import com.github.rafaelfernandes.service.application.domain.model.Service;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.time.LocalDate;

public interface ManageServiceUseCase {

    Service.ReservationId create(Restaurant restaurant, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables);

    Service details(Service.ReservationId reservationId);

    Boolean update(Service reservation);


}
