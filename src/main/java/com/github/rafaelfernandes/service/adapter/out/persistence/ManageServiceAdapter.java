package com.github.rafaelfernandes.service.adapter.out.persistence;

import com.github.rafaelfernandes.common.annotations.PersistenceAdapter;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.service.application.domain.model.Service;
import com.github.rafaelfernandes.service.application.port.out.ManageServicePort;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class ManageServiceAdapter implements ManageServicePort {

    private final ServiceRepository repository;
    private final ServicePersistenceMapper mapper;

    @Override
    public Boolean existsService(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date) {

        return !repository
                .findByCriteria(
                        UUID.fromString(restaurantId.id()),
                        date,
                        openingHour.getDayOfWeek(),
                        openingHour.getStart(),
                        openingHour.getEnd()
                )
                .isEmpty();

    }

    @Override
    public Service save(Service service) {

        var entity = mapper.toEntity(service);

        ServiceJpaEntity savedEntity = repository.save(entity);

        return mapper.toModel(savedEntity);
    }


    @Override
    public Service details(Service.ServiceId reservationId) {
        return null;
    }
}
