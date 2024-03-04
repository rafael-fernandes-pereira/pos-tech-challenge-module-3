package com.github.rafaelfernandes.restaurant.application.domain.service;

import com.github.rafaelfernandes.restaurant.application.port.in.CreateRestaurantCommand;
import com.github.rafaelfernandes.restaurant.application.port.in.SaveDataRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.common.annotations.UseCase;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@UseCase
@RequiredArgsConstructor
public class SaveDataRestaurantService implements SaveDataRestaurantUseCase {

    @Override
    public UUID create(CreateRestaurantCommand command) {
        return null;
    }
}
