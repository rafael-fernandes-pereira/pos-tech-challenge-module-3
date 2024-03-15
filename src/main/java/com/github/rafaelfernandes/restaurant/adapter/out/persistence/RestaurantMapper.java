package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.domain.Restaurant;
import com.github.rafaelfernandes.restaurant.common.enums.Cuisine;
import com.github.rafaelfernandes.restaurant.common.enums.State;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

@Component
class RestaurantMapper {

    RestaurantJpaEntity toCreateEntity(Restaurant restaurant){

        var restaurantEntity = new RestaurantJpaEntity();
        restaurantEntity.setId(restaurant.getRestaurantId().getValue());
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
                restaurantJpaEntity.getId(),
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
