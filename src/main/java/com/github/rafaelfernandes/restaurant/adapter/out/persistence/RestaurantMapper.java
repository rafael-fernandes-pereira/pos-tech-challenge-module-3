package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

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
        addressEntity.setState(restaurant.getAddress().getState().name().toUpperCase());


        restaurantEntity.setOpeningHours(openingHoursEntity);
        restaurantEntity.setAddress(addressEntity);

        return restaurantEntity;






    }

}
