package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.common.exception.RestaurantDuplicateException;
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
    class Create {

        @Test
        void saveNewRestaurantSucess(){

            var restaurant = GenerateData.createRestaurant();
            var restaurantId = restaurantPersistenceAdapter.create(restaurant);

            assertThat(restaurant.getRestaurantId()).isEqualTo(restaurantId);

            var restaurantIdUUID = UUID.fromString(restaurantId.id());

            var restaurantSaved = restaurantRepository.findById(restaurantIdUUID);

            assertThat(restaurantSaved).isPresent();
            assertThat(restaurant.getName()).isEqualTo(restaurantSaved.get().getName());
            assertThat(restaurant.getTables()).isEqualTo(restaurantSaved.get().getTables());
            assertThat(restaurant.getAddress().getStreet()).isEqualTo(restaurantSaved.get().getAddress().getStreet());

            var openingHours = restaurantMapper.toOpeningHoursModel(restaurantSaved.get().getOpeningHours());

            assertThat(openingHours).isEqualTo(restaurant.getOpeningHours());




        }

        @Test
        void saveDuplicateRestaurantError(){

            var restaurant = GenerateData.createRestaurant();
            restaurantPersistenceAdapter.create(restaurant);

            assertThatThrownBy(() -> {
                restaurantPersistenceAdapter.create(restaurant);
            })
                    .isInstanceOf(RestaurantDuplicateException.class)
                    .hasMessage("Nome já cadastrado!");

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