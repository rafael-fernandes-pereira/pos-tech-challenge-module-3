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
public class ManageServiceService implements com.github.rafaelfernandes.service.application.port.in.ManageServiceUseCase {

    private final ManageServicePort manageReservationPort;

    @Override
    public Service.ServiceId create(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables) {
        var exists = manageReservationPort.existsService(restaurantId, openingHour, date);

        if (exists) throw new ReservationDuplicateException();

        var resservation = manageReservationPort.save(restaurantId, openingHour, date, tables);

        return resservation.getServiceId();

    }

    @Override
    public Service details(Service.ServiceId reservationId) {
        return manageReservationPort.details(reservationId);
    }

    @Override
    public Boolean update(Service reservation) {
        return null;
    }
}
