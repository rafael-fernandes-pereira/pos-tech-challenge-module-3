package com.github.rafaelfernandes.service.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.service.application.domain.model.Service;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ServicePersistenceMapper {

    ServiceJpaEntity toEntity (Service service){

        var entity = new ServiceJpaEntity();
        entity.setId(UUID.fromString(service.getServiceId().id()));
        entity.setRestaurantId(UUID.fromString(service.getRestaurantId().id()));
        entity.setDate(service.getDate());
        entity.setDayOfWeek(service.getOpeningHour().getDayOfWeek());
        entity.setStart(service.getOpeningHour().getStart());
        entity.setEnd(service.getOpeningHour().getEnd());
        entity.setTables(service.getTables());

        return entity;

    }

    Service toModel (ServiceJpaEntity entity){

        return new Service(
                entity.getId().toString(),
                new Restaurant.RestaurantId(entity.getRestaurantId().toString()),
                new Restaurant.OpeningHour(
                        entity.getDayOfWeek(),
                        entity.getStart(),
                        entity.getEnd()
                ),
                entity.getDate(), entity.getTables()
        );

    }

}
