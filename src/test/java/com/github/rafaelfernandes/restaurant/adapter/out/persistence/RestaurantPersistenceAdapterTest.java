package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.util.GenerateData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import({RestaurantPersistenceAdapter.class, RestaurantMapper.class})
class RestaurantPersistenceAdapterTest {

    @Autowired
    private RestaurantPersistenceAdapter restaurantPersistenceAdapter;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Nested
    class Create {

        @Test
        void saveNewRestaurantSucess(){

            var restaurant = GenerateData.createRestaurant();
            var restaurantId = restaurantPersistenceAdapter.create(restaurant);

            assertThat(restaurant.getRestaurantId()).isEqualTo(restaurantId);

            var restaurantSaved = restaurantRepository.findById(restaurantId.getValue());

            assertThat(restaurant.getName()).isEqualTo(restaurantSaved.get().getName());
            assertThat(restaurant.getTables()).isEqualTo(restaurantSaved.get().getTables());
            assertThat(restaurant.getAddress().getStreet()).isEqualTo(restaurantSaved.get().getAddress().getStreet());

        }

    }


}