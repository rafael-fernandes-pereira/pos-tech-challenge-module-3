package com.github.rafaelfernandes.reservation.application.domain.service;

import com.github.rafaelfernandes.common.annotations.UseCase;
import com.github.rafaelfernandes.common.exception.ReservationDuplicateException;
import com.github.rafaelfernandes.reservation.application.domain.model.Reservation;
import com.github.rafaelfernandes.reservation.application.port.out.ManageReservationPort;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@UseCase
@RequiredArgsConstructor
public class ManageReservationUseCase implements com.github.rafaelfernandes.reservation.application.port.in.ManageReservationUseCase {

    private final ManageReservationPort manageReservationPort;

    @Override
    public Reservation.ReservationId create(Restaurant restaurant, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables) {
        var exists = manageReservationPort.existsReservation(restaurant.getRestaurantId(), openingHour, date);

        if (exists) throw new ReservationDuplicateException();

        var resservation = manageReservationPort.save(restaurant, openingHour, date, tables);

        return resservation.getReservationId();

    }

    @Override
    public Reservation details(Reservation.ReservationId reservationId) {
        return null;
    }

    @Override
    public Boolean update(Reservation reservation) {
        return null;
    }
}
