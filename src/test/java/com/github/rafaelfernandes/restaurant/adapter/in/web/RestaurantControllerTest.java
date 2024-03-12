package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.application.port.in.CreateRestaurantCommand;
import com.github.rafaelfernandes.restaurant.application.port.in.GetRestarauntDataCommand;
import com.github.rafaelfernandes.restaurant.application.port.in.GetRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.application.port.in.SaveDataRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantDuplicateException;
import com.github.rafaelfernandes.restaurant.common.exception.RestaurantNotFoundException;
import util.GenerateData;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RestaurantController.class)
class RestaurantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SaveDataRestaurantUseCase saveDataRestaurantUseCase;

    @MockBean
    private GetRestaurantUseCase getRestaurantUseCase;

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

    @Nested
    class FindById{

        @Test
        void validateCommandError() throws Exception {
            mockMvc.perform(
                            get("/restaurants/uuid-invalid")
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.errors").value("restaurantId: O campo deve ser do tipo UUID"))
            ;
        }

        @Test
        void validateNotFound() throws Exception {

            when(getRestaurantUseCase.findBy(any(GetRestarauntDataCommand.class)))
                    .thenThrow(RestaurantNotFoundException.class);

            mockMvc.perform(
                            get("/restaurants/e903732e-9d20-4023-a71a-5c761253fc1c")
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.errors").value("Restaurante(s) não existe!"))
            ;
        }

        @Test
        void validateFound() throws Exception {

            var restaurant = Optional.of(GenerateData.createRestaurant());

            when(getRestaurantUseCase.findBy(any(GetRestarauntDataCommand.class)))
                    .thenReturn(restaurant);

            mockMvc.perform(
                            get("/restaurants/e903732e-9d20-4023-a71a-5c761253fc1c")
                                    .contentType(MediaType.APPLICATION_JSON)
                    )
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.id").value(restaurant.get().getRestaurantId().getValue().toString()))
                    .andExpect(jsonPath("$.name").value(restaurant.get().getName()))
                    .andExpect(jsonPath("$.address.street").value(restaurant.get().getAddress().getStreet()))
                    .andExpect(jsonPath("$.address.number").value(restaurant.get().getAddress().getNumber()))
                    .andExpect(jsonPath("$.address.addittionalDetails").value(restaurant.get().getAddress().getAddittionalDetails()))
                    .andExpect(jsonPath("$.address.neighborhood").value(restaurant.get().getAddress().getNeighborhood()))
                    .andExpect(jsonPath("$.address.city").value(restaurant.get().getAddress().getCity()))
                    .andExpect(jsonPath("$.address.state").value(restaurant.get().getAddress().getState().name()))
            ;

        }


    }



}