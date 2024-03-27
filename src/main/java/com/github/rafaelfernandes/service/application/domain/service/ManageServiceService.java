package com.github.rafaelfernandes.service.application.domain.service;

import com.github.rafaelfernandes.common.annotations.UseCase;
import com.github.rafaelfernandes.service.exception.ServiceDuplicateException;
import com.github.rafaelfernandes.restaurant.application.port.in.ManageRestaurantUseCase;
import com.github.rafaelfernandes.service.application.domain.model.Service;
import com.github.rafaelfernandes.service.application.port.out.ManageServicePort;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@UseCase
@RequiredArgsConstructor
public class ManageServiceService implements com.github.rafaelfernandes.service.application.port.in.ManageServiceUseCase {

    private final ManageServicePort manageServicePort;
    private final ManageRestaurantUseCase manageRestaurantUseCase;

    @Override
    public Service.ServiceId create(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables) {

        var exists = manageServicePort.existsService(restaurantId, openingHour, date);

        if (Boolean.TRUE.equals(exists)) throw new ServiceDuplicateException();

        var restaurant = manageRestaurantUseCase.findById(restaurantId);

        var serviceToSave = new Service(restaurant, openingHour, date, tables);

        var service = manageServicePort.save(serviceToSave);

        return service.getServiceId();

    }

    @Override
    public Service details(Service.ServiceId reservationId) {
        return manageServicePort.details(reservationId);
    }

    @Override
    public Boolean update(Service reservation) {
        return null;
    }
}
