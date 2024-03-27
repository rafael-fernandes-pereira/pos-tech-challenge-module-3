package com.github.rafaelfernandes.service.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.adapter.in.web.response.RestaurantResponse;
import com.github.rafaelfernandes.restaurant.adapter.out.persistence.RestaurantRepository;
import com.github.rafaelfernandes.service.adapter.in.web.request.OpeningHourServiceRequest;
import com.github.rafaelfernandes.service.adapter.in.web.request.ServiceRequest;
import com.github.rafaelfernandes.service.adapter.in.web.response.ServiceResponse;
import com.github.rafaelfernandes.service.adapter.out.persistence.ServiceRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import util.GenerateData;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ServiceControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @BeforeEach
    void setup(){
        restTemplate.getRestTemplate().setInterceptors(
                Collections.singletonList((request, body, execution) -> {
                    request.getHeaders()
                            .add("Content-Type", MediaType.APPLICATION_JSON_VALUE);
                    return execution.execute(request, body);
                }));
    }

    @AfterEach
    void tearDown(){
        serviceRepository.deleteAll();
    }

    private ResponseEntity<RestaurantResponse> createRestaurant(RestaurantRequest request) {
        var create =  restTemplate
                .postForEntity(
                        "/restaurants/",
                        request,
                        RestaurantResponse.class
                );

        var response = restTemplate
                .getForEntity(
                        create.getHeaders().getLocation(),
                        RestaurantResponse.class
                );

        return response;

    }

    @Nested
    class Create {

        @Test
        void createSuccess() {

            RestaurantRequest request = GenerateData.gerenRestaurantRequest();

            var responseRestaurant = createRestaurant(request);

            OpeningHourServiceRequest openingHourServiceRequest = new OpeningHourServiceRequest(
                    responseRestaurant.getBody().opening_hour().get(0).day_of_week(),
                    responseRestaurant.getBody().opening_hour().get(0).start(),
                    responseRestaurant.getBody().opening_hour().get(0).end()
            );

            ServiceRequest serviceRequest = new ServiceRequest(
                    responseRestaurant.getBody().id().toString(),
                    openingHourServiceRequest,
                    LocalDate.now().plusDays(2),
                    responseRestaurant.getBody().tables()
            );

            var response = restTemplate
                    .postForEntity(
                            "/services/",
                            serviceRequest,
                            ServiceResponse.class
                    );

            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);


        }

    }


}