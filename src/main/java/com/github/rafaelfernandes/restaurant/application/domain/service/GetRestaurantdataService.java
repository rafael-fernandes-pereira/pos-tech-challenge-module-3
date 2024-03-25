package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.GetRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.application.port.out.GetRestaurantPort;
import com.github.rafaelfernandes.common.annotations.UseCase;
import com.github.rafaelfernandes.common.enums.Cuisine;
import com.github.rafaelfernandes.common.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetRestaurantdataService implements GetRestaurantUseCase {

    private final GetRestaurantPort getRestaurantPort;

    @Override
    public List<Restaurant> findAllBy(String name, String location, List<Cuisine> cuisines) {

        var restaurants = getRestaurantPort.findAllBy(
                name,
                location,
                cuisines);

        if (restaurants.isEmpty())  throw new RestaurantNotFoundException();

        return restaurants;

    }
}
