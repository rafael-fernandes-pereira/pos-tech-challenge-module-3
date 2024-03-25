package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.common.annotations.UseCase;
import com.github.rafaelfernandes.common.enums.Cuisine;
import com.github.rafaelfernandes.common.exception.RestaurantDuplicateException;
import com.github.rafaelfernandes.common.exception.RestaurantNotFoundException;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.ManageRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.application.port.out.ManageRestaurantPort;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;

@UseCase
@RequiredArgsConstructor
public class ManageRestaurantService implements ManageRestaurantUseCase {

    private final ManageRestaurantPort manageRestaurantPort;

    @Override
    @Transactional
    public Restaurant.RestaurantId create(Restaurant restaurant) {

        if (manageRestaurantPort.existsName(restaurant.getName())) throw new RestaurantDuplicateException();

        var restaurantNew = manageRestaurantPort.save(restaurant);

        return restaurantNew.getRestaurantId();

    }

    @Override
    public Restaurant findById(Restaurant.RestaurantId restaurantId) {

        return manageRestaurantPort.findById(restaurantId)
                .orElseThrow(RestaurantNotFoundException::new);

    }

    @Override
    public List<Restaurant> findAllBy(String name, String location, List<Cuisine> cuisines) {

        var list = manageRestaurantPort.findAllBy(name, location, cuisines);

        if (list.isEmpty()) throw new RestaurantNotFoundException();

        return list;

    }
}
