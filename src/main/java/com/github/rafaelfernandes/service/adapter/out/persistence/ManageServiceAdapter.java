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

    private final ServiceRepository serviceRepository;

    @Override
    public Boolean existsService(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date) {

        return !serviceRepository.findByCriteria(UUID.fromString(restaurantId.id()), date, openingHour.getDayOfWeek(), openingHour.getStart(), openingHour.getEnd()).isEmpty();

    }

    @Override
    public Service save(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables) {
        // Criar uma instância de ServiceJpaEntity
        ServiceJpaEntity serviceEntity = new ServiceJpaEntity();
        serviceEntity.setId(UUID.randomUUID()); // Gera um novo UUID para o serviço
        serviceEntity.setRestaurantId(UUID.fromString(restaurantId.id())); // Converte o UUID do restaurante
        serviceEntity.setDate(date);
        serviceEntity.setDayOfWeek(openingHour.getDayOfWeek());
        serviceEntity.setStart(openingHour.getStart());
        serviceEntity.setEnd(openingHour.getEnd());
        serviceEntity.setTables(tables);

        // Salvar a entidade usando o repository
        ServiceJpaEntity savedEntity = serviceRepository.save(serviceEntity);

        // Criar e retornar um objeto Service com base na entidade salva
        return new Service(savedEntity.getId().toString(), new Restaurant.RestaurantId(savedEntity.getRestaurantId().toString()),
                new Restaurant.OpeningHour(savedEntity.getDayOfWeek(), savedEntity.getStart(), savedEntity.getEnd()),
                savedEntity.getDate(), savedEntity.getTables());
    }


    @Override
    public Service details(Service.ServiceId reservationId) {
        return null;
    }
}
