package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
class RestaurantMapper {

    RestaurantJpaEntity toCreateEntity(Restaurant restaurant){

        var restaurantEntity = new RestaurantJpaEntity();
        restaurantEntity.setId(UUID.fromString(restaurant.getRestaurantId().id()));
        restaurantEntity.setName(restaurant.getName());
        restaurantEntity.setRegister(restaurant.getRegister());
        restaurantEntity.setTables(restaurant.getTables());

        var openingHoursEntity = new ArrayList<OpeningHourJpaEntity>();

        for (Restaurant.OpeningHour openingHour: restaurant.getOpeningHours()){
            var openingHourEntity = new OpeningHourJpaEntity();
            openingHourEntity.setDayOfWeek(openingHour.getDayOfWeek().toUpperCase());
            openingHourEntity.setStart(openingHour.getStart());
            openingHourEntity.setEnd(openingHour.getEnd());
            openingHourEntity.setRestaurant(restaurantEntity);
            openingHoursEntity.add(openingHourEntity);

        }

        var addressEntity = new AddressJpaEntity();

        addressEntity.setStreet(restaurant.getAddress().getStreet());
        addressEntity.setNumber(restaurant.getAddress().getNumber());
        addressEntity.setAddittionalDetails(restaurant.getAddress().getAddittionalDetails());
        addressEntity.setNeighborhood(restaurant.getAddress().getNeighborhood());
        addressEntity.setCity(restaurant.getAddress().getCity());
        addressEntity.setState(restaurant.getAddress().getState().toUpperCase());

        var fullSearch = new StringBuilder()
                .append(restaurant.getName())
                .append("_")
                .append(restaurant.getAddress().getStreet())
                .append("_")
                .append(restaurant.getAddress().getNumber())
                .append("_")
                .append(restaurant.getAddress().getAddittionalDetails())
                .append("_")
                .append(restaurant.getAddress().getNeighborhood())
                .append("_")
                .append(restaurant.getAddress().getCity())
                .append("_")
                .append(restaurant.getAddress().getState().toUpperCase())
                .append("_")
        ;

        var cuisines = new ArrayList<CuisineJpaEntity>();

        for (Restaurant.Cuisine cuisine : restaurant.getCuisines()){
            var cuisineJpa =  new CuisineJpaEntity();
            cuisineJpa.setCusine(cuisine.getCuisine());
            cuisines.add(cuisineJpa);
            fullSearch.append(cuisine.getCuisine().toUpperCase()).append("_");
        }

        restaurantEntity.setFullSearch(fullSearch.toString());

        restaurantEntity.setOpeningHours(openingHoursEntity);
        restaurantEntity.setAddress(addressEntity);
        restaurantEntity.setCuisines(cuisines);

        return restaurantEntity;
    }

    Restaurant toModel(RestaurantJpaEntity restaurantJpaEntity){

        Restaurant.Address address = new Restaurant.Address(
                restaurantJpaEntity.getAddress().getStreet(),
                restaurantJpaEntity.getAddress().getNumber(),
                restaurantJpaEntity.getAddress().getAddittionalDetails(),
                restaurantJpaEntity.getAddress().getNeighborhood(),
                restaurantJpaEntity.getAddress().getCity(),
                restaurantJpaEntity.getAddress().getState()
        );

        var openinHours = toOpeningHoursModel(restaurantJpaEntity.getOpeningHours());

        var cuisines = restaurantJpaEntity.getCuisines() == null ?

                new ArrayList<Restaurant.Cuisine>() :

                restaurantJpaEntity.getCuisines().stream()
                .map(cuisineJpaEntity -> new Restaurant.Cuisine(cuisineJpaEntity.getCusine()))
                .toList();

        return Restaurant.of(
                restaurantJpaEntity.getId().toString(),
                restaurantJpaEntity.getName(),
                address,
                restaurantJpaEntity.getRegister(),
                openinHours,
                restaurantJpaEntity.getTables(),
                cuisines
        );
    }

    List<Restaurant.OpeningHour> toOpeningHoursModel(List<OpeningHourJpaEntity> openingHourJpaEntities) {
        return openingHourJpaEntities.stream()
                .map(openingHourJpaEntity -> new Restaurant.OpeningHour(
                        openingHourJpaEntity.getDayOfWeek(),
                        openingHourJpaEntity.getStart(),
                        openingHourJpaEntity.getEnd()
                )).toList();

    }

}
