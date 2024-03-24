package com.github.rafaelfernandes.reservation.application.port.in;

import com.github.rafaelfernandes.reservation.application.domain.model.Reservation;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.time.LocalDate;

public interface ManageReservationUseCase {

    Reservation.ReservationId create(Restaurant restaurant, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables);

    Reservation details(Reservation.ReservationId reservationId);

    Boolean update(Reservation reservation);


}
