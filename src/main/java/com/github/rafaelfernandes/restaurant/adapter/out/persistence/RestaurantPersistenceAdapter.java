package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.out.CreateRestaurantPort;
import com.github.rafaelfernandes.restaurant.application.port.out.GetRestaurantPort;
import com.github.rafaelfernandes.restaurant.common.annotations.PersistenceAdapter;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantDuplicateException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.UUID;

@PersistenceAdapter
@RequiredArgsConstructor
public class RestaurantPersistenceAdapter implements CreateRestaurantPort, GetRestaurantPort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    @Transactional
    public Restaurant.RestaurantId create(Restaurant restaurant) throws RestaurantDuplicateException {

        if (restaurantRepository.existsByName(restaurant.getName())) throw new RestaurantDuplicateException();

        var restaurantToSave = restaurantMapper.toCreateEntity(restaurant);

        var restautarantSaved = restaurantRepository.save(restaurantToSave);

        return new Restaurant.RestaurantId(restautarantSaved.getId());

    }

    @Override
    @Transactional
    public Optional<Restaurant> findById(UUID id) {

        var restaurantData = restaurantRepository.findById(id);

        if (restaurantData.isEmpty()) return Optional.empty();

        var restaraunt = restaurantMapper.toModel(restaurantData.get());

        return Optional.ofNullable(restaraunt);
    }
}
