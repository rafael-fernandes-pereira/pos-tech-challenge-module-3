package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.common.exception.RestaurantDuplicateException;
import util.GenerateData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Import({RestaurantPersistenceAdapter.class, RestaurantMapper.class})
class RestaurantPersistenceAdapterTest {

    @Autowired
    private RestaurantPersistenceAdapter restaurantPersistenceAdapter;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private RestaurantMapper restaurantMapper;

    @AfterEach
    void tearDown(){
        restaurantRepository.deleteAll();
    }


    @Nested
    class ExistsName {

        @Test
        void validateTrue(){
            // Arrange
            var restaurant = GenerateData.createRestaurant();
            var entity = restaurantMapper.toCreateEntity(restaurant);
            restaurantRepository.save(entity);

            // Act

            var isExists = restaurantPersistenceAdapter.existsName(restaurant.getName());

            // Assert

            assertThat(isExists).isTrue();


        }

        @Test
        void validateFalse() {

            // Act

            var isExists = restaurantPersistenceAdapter.existsName("Banana");

            // Assert

            assertThat(isExists).isFalse();

        }

    }

    @Nested
    class Save {

        @Test
        void validateSuccessSave(){

            var restaurant = GenerateData.createRestaurant();
            var restaurantSaved = restaurantPersistenceAdapter.save(restaurant);

            assertThat(restaurant.getRestaurantId()).isEqualTo(restaurantSaved.getRestaurantId());

            var restaurantIdUUID = UUID.fromString(restaurantSaved.getRestaurantId().id());

            var restaurantFound = restaurantRepository.findById(restaurantIdUUID);

            assertThat(restaurantFound).isPresent();
            assertThat(restaurantFound.get().getFullSearch()).isNotEmpty();

        }

    }

    @Nested
    class FindById {


        @Test
        void findSuccess(){

            var restaurant = GenerateData.createRestaurant();
            var restaurantId = restaurantPersistenceAdapter.create(restaurant);

            var restaurantIdUUID = UUID.fromString(restaurantId.id());

            var restaurantGet = restaurantPersistenceAdapter.findById(restaurantIdUUID);

            assertThat(restaurantGet).isPresent();
            assertThat(restaurantId).isEqualTo(restaurantGet.get().getRestaurantId());
            assertThat(restaurant.getName()).isEqualTo(restaurantGet.get().getName());
            assertThat(restaurant.getOpeningHours()).isEqualTo(restaurantGet.get().getOpeningHours());
            assertThat(restaurant.getTables()).isEqualTo(restaurantGet.get().getTables());
            assertThat(restaurant.getCuisines()).isEqualTo(restaurantGet.get().getCuisines());
        }

        @Test
        void notFound(){

            var restaurantGet = restaurantPersistenceAdapter.findById(UUID.fromString("0b02d081-b452-4edf-bc09-039bad8de53a"));

            assertThat(restaurantGet).isEmpty();

        }



    }

    @Nested
    class FindAllBy {

        @Test
        void findByAllByNameSucess(){

            var restaurant = GenerateData.createRestaurant();
            restaurantPersistenceAdapter.create(restaurant);

            var restaurants = restaurantPersistenceAdapter.findAllBy(restaurant.getName(), "", null);

            assertThat(restaurants).hasSize(1);

        }

    }

}