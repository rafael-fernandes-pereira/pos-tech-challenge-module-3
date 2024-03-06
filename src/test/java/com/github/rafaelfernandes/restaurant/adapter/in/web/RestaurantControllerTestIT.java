package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.util.GenerateData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestaurantControllerTestIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Nested
    class Create {

        @Test
        void createSuccess(){

            RestaurantRequest request = GenerateData.gerenRestaurantRequest();

            ResponseEntity<Void> createResponse = restTemplate
                    .postForEntity(
                            "/restaurants/",
                            request,
                            Void.class
                    );

            assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        }

    }

}
