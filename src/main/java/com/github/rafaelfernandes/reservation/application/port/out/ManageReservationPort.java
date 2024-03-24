package com.github.rafaelfernandes.reservation.application.port.out;

import com.github.rafaelfernandes.reservation.application.domain.model.Reservation;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;

import java.time.LocalDate;

public interface ManageReservationPort {

    Boolean existsReservation(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date);

    Reservation save(Restaurant restaurant, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables);

    Reservation details(Reservation.ReservationId reservationId);

}
