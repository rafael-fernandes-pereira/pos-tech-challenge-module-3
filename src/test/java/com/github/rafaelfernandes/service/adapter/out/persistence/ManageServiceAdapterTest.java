package com.github.rafaelfernandes.service.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.service.application.domain.model.Service;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import util.GenerateData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import({ManageServiceAdapter.class, ServicePersistenceMapper.class})
class ManageServiceAdapterTest {

    @Autowired
    private ManageServiceAdapter adapter;

    @Autowired
    private ServicePersistenceMapper mapper;

    @Autowired
    private ServiceRepository repository;

    @AfterEach
    void tearDown(){
        repository.deleteAll();
    }

    @Nested
    class ExistsService {

        Restaurant.OpeningHour openingHour;
        Restaurant.RestaurantId restaurantId;

        LocalDate date;
        @BeforeEach
        void setUp(){

            UUID serviceId = UUID.fromString("0826faa6-4157-47d0-a0e8-c645c71bbe63");
            UUID restaurantIdServiceId = UUID.fromString("4cc6b67d-63ee-4b75-8504-f48d9a200861");
            String dayOfWeek = DayOfWeek.MONDAY.name();
            LocalTime start = LocalTime.of(10,0);
            LocalTime end = LocalTime.of(14,0);
            Integer tables = 10;

            date = LocalDate.now();

            openingHour = new Restaurant.OpeningHour(
                    dayOfWeek, start, end
            );

            restaurantId = new Restaurant.RestaurantId(restaurantIdServiceId.toString());

            ServiceJpaEntity entity = new ServiceJpaEntity();
            entity.setId(serviceId);
            entity.setRestaurantId(restaurantIdServiceId);
            entity.setDate(date);
            entity.setDayOfWeek(dayOfWeek);
            entity.setStart(start);
            entity.setEnd(end);
            entity.setTables(tables);

            repository.save(entity);


        }

        @Test
        void validateServiceExistsSame() {

            // Act
            var exists = adapter.existsService(restaurantId, openingHour, date);

            // Assert
            assertThat(exists).isTrue();

        }

        @Test
        void validateServiceExistsBetweenStartEnd() {

            // Arrange
            openingHour = new Restaurant.OpeningHour(
                    DayOfWeek.MONDAY.name(),
                    LocalTime.of(11,0),
                    LocalTime.of(12,0)
            );

            // Act
            var exists = adapter.existsService(restaurantId, openingHour, date);

            // Assert
            assertThat(exists).isTrue();

        }

        @Test
        void calidateServiceNotExists(){

            // Arrange
            restaurantId = new Restaurant.RestaurantId("e5756cc4-2c05-4ca9-8eac-679fc40dc941");

            // Act
            var exists = adapter.existsService(restaurantId, openingHour, date);

            // Assert
            assertThat(exists).isFalse();

        }

    }

    @Nested
    class Save {

        @Test
        void validateSave(){

            // Arrange
            var restaurant = GenerateData.createRestaurant();

            var openingHour = restaurant.getOpeningHours().get(0);

            var date = LocalDate.now().plusDays(2);

            var tables = 1;

            var service = new Service(restaurant, openingHour, date, tables);

            // Act
            var serviceSaved = adapter.save(service);

            // Assert
            assertThat(serviceSaved.getServiceId()).isEqualTo(service.getServiceId());
            assertThat(serviceSaved.getRestaurantId()).isEqualTo(service.getRestaurantId());
            assertThat(serviceSaved.getOpeningHour()).isEqualTo(service.getOpeningHour());
            assertThat(serviceSaved.getDate()).isEqualTo(service.getDate());
            assertThat(serviceSaved.getTables()).isEqualTo(service.getTables());


        }

    }

}