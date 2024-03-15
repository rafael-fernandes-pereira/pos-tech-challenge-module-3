package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.restaurant.domain.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.GetRestarauntDataCommand;
import com.github.rafaelfernandes.restaurant.application.port.in.GetRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.application.port.out.GetRestaurantPort;
import com.github.rafaelfernandes.restaurant.common.annotations.UseCase;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantNotFoundException;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class GetRestaurantdataService implements GetRestaurantUseCase {

    private final GetRestaurantPort getRestaurantPort;

    @Override
    public Optional<Restaurant> findBy(GetRestarauntDataCommand getRestarauntDataCommand) {

        if (Optional.ofNullable(getRestarauntDataCommand.getRestaurantId()).isPresent()){

            var restarauntId = UUID.fromString(getRestarauntDataCommand.getRestaurantId());

            var restaurant = getRestaurantPort.findById(restarauntId);

            if (restaurant.isEmpty()) throw new RestaurantNotFoundException();

            return restaurant;

        }

        throw new RestaurantNotFoundException();

    }

    @Override
    public List<Restaurant> findAlldBy(GetRestarauntDataCommand getRestarauntDataCommand) {

        var restaurants = getRestaurantPort.findAllBy(
                getRestarauntDataCommand.getName(),
                getRestarauntDataCommand.getLocation(),
                getRestarauntDataCommand.getCuisines(),
                getRestarauntDataCommand.getPage(),
                getRestarauntDataCommand.getQuantity(),
                getRestarauntDataCommand.getOrderBy());

        if (restaurants.isEmpty())  throw new RestaurantNotFoundException();

        return restaurants;

    }
}
