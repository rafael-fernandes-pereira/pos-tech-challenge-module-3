package com.github.rafaelfernandes.service.application.domain.service;

import com.github.rafaelfernandes.common.annotations.UseCase;
import com.github.rafaelfernandes.common.exception.ReservationDuplicateException;
import com.github.rafaelfernandes.service.application.domain.model.Service;
import com.github.rafaelfernandes.service.application.port.out.ManageServicePort;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@UseCase
@RequiredArgsConstructor
public class ManageServiceUseCase implements com.github.rafaelfernandes.service.application.port.in.ManageServiceUseCase {

    private final ManageServicePort manageReservationPort;

    @Override
    public Service.ReservationId create(Restaurant restaurant, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables) {
        var exists = manageReservationPort.existsReservation(restaurant.getRestaurantId(), openingHour, date);

        if (exists) throw new ReservationDuplicateException();

        var resservation = manageReservationPort.save(restaurant, openingHour, date, tables);

        return resservation.getReservationId();

    }

    @Override
    public Service details(Service.ReservationId reservationId) {
        return null;
    }

    @Override
    public Boolean update(Service reservation) {
        return null;
    }
}
