package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.out.CreateRestaurantPort;
import com.github.rafaelfernandes.restaurant.common.annotations.PersistenceAdapter;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantDuplicateException;
import lombok.RequiredArgsConstructor;

@PersistenceAdapter
@RequiredArgsConstructor
public class RestaurantPersistenceAdapter implements CreateRestaurantPort {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    @Override
    public Restaurant.RestaurantId create(Restaurant restaurant) throws RestaurantDuplicateException {

        var restaurantToSave = restaurantMapper.toCreateEntity(restaurant);

        var restautarantSaved = restaurantRepository.save(restaurantToSave);

        return new Restaurant.RestaurantId(restautarantSaved.getId());

    }
}
