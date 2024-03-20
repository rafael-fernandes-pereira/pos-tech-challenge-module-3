package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.common.enums.Cuisine;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
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
            openingHourEntity.setDayOfWeek(openingHour.getDayOfWeek().name().toUpperCase());
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

        for (Cuisine cuisine : restaurant.getCuisines()){
            fullSearch.append(cuisine.name().toUpperCase()).append("_");
        }

        restaurantEntity.setFullSearch(fullSearch.toString());


        restaurantEntity.setOpeningHours(openingHoursEntity);
        restaurantEntity.setAddress(addressEntity);

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

                new ArrayList<Cuisine>() :

                restaurantJpaEntity.getCuisines().stream()
                .map(cuisineJpaEntity -> Cuisine.valueOf(cuisineJpaEntity.getCusine()))
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
                        DayOfWeek.valueOf(openingHourJpaEntity.getDayOfWeek()),
                        openingHourJpaEntity.getStart(),
                        openingHourJpaEntity.getEnd()
                )).toList();

    }

}
