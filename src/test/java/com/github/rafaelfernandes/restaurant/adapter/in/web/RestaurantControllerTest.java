package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.request.AddressRequest;
import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.CreateRestaurantCommand;
import com.github.rafaelfernandes.restaurant.application.port.in.SaveDataRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantDuplicateException;
import com.github.rafaelfernandes.restaurant.util.GenerateData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaveDataRestaurantUseCase saveDataRestaurantUseCase;

    @Nested
    class Create {
        @Test
        void validateCreateRestaurantCommandError() throws Exception {

            var address = GenerateData.generateAddressRequest();

            String name = null;

            RestaurantRequest restaurant = new RestaurantRequest(name, address);

            mockMvc.perform(
                            post("/restaurants/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(GenerateData.asJsonString(restaurant))
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors").value("name: O campo deve estar preenchido"))
            ;

        }

        @Test
        void validateDuplicateRestaurantError() throws Exception {

            var restaurant = GenerateData.gerenRestaurantRequest();

            when(saveDataRestaurantUseCase.create(any(CreateRestaurantCommand.class)))
                    .thenThrow(RestaurantDuplicateException.class);

            mockMvc.perform(
                            post("/restaurants/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(GenerateData.asJsonString(restaurant))
                    )
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("$.errors").value("Nome já cadastrado!"))
            ;

        }

        @Test
        void validateCreateSucess() throws Exception {

            var restaurant = GenerateData.gerenRestaurantRequest();

            var restaurantId = new Restaurant.RestaurantId(UUID.randomUUID());

            when(saveDataRestaurantUseCase.create(any(CreateRestaurantCommand.class)))
                    .thenReturn(restaurantId);

            mockMvc.perform(
                            post("/restaurants/")
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .content(GenerateData.asJsonString(restaurant))
                    )
                    .andExpect(status().isCreated())
                    .andExpect(header().string("Location", containsString("/restaurants/"+restaurantId.getValue().toString())));

            ;

        }
    }



}