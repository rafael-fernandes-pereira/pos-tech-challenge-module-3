package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.common.annotations.UseCase;
import com.github.rafaelfernandes.common.enums.Cuisine;
import com.github.rafaelfernandes.common.exception.RestaurantDuplicateException;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.ManageRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.application.port.out.ManageRestaurantPort;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class ManageRestaurantService implements ManageRestaurantUseCase {

    private final ManageRestaurantPort manageRestaurantPort;

    @Override
    public Restaurant.RestaurantId create(Restaurant restaurant) {
        if (manageRestaurantPort.existsName(restaurant.getName())) throw new RestaurantDuplicateException();

        var restaurantNew = manageRestaurantPort.save(restaurant);

        return restaurantNew.getRestaurantId();

    }

    @Override
    public Optional<Restaurant> findById(Restaurant.RestaurantId restaurantId) {
        return Optional.empty();
    }

    @Override
    public List<Restaurant> findAllBy(String name, String location, List<Cuisine> cuisines) {
        return null;
    }
}
