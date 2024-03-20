package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.out.CreateRestaurantPort;
import com.github.rafaelfernandes.restaurant.application.port.out.GetRestaurantPort;
import com.github.rafaelfernandes.restaurant.common.annotations.PersistenceAdapter;
import com.github.rafaelfernandes.restaurant.common.enums.Cuisine;
import com.github.rafaelfernandes.restaurant.common.enums.OrderBy;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantDuplicateException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;
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

        return new Restaurant.RestaurantId(restautarantSaved.getId().toString());

    }

    @Override
    @Transactional
    public Optional<Restaurant> findById(UUID id) {

        var restaurantData = restaurantRepository.findById(id);

        if (restaurantData.isEmpty()) return Optional.empty();

        var restaraunt = restaurantMapper.toModel(restaurantData.get());

        return Optional.ofNullable(restaraunt);
    }

    @Override
    public List<Restaurant> findAllBy(String name, String location, List<Cuisine> cuisines) {
        var restaurantRepositoryPage = restaurantRepository.findRestaurantsByCriteria(
            name, location, cuisines
        );

        if (restaurantRepositoryPage.isEmpty()) return new ArrayList<>();

        return restaurantRepositoryPage.stream()
                .map(restaurantMapper::toModel)
                .toList();



    }
}
