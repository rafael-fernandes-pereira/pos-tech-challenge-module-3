package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.adapter.in.web.response.AddressResponse;
import com.github.rafaelfernandes.restaurant.adapter.in.web.response.RestaurantError;
import com.github.rafaelfernandes.restaurant.adapter.in.web.response.RestaurantResponse;
import com.github.rafaelfernandes.restaurant.application.port.in.*;
import com.github.rafaelfernandes.restaurant.common.annotations.WebAdapter;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.restaurant.common.enums.Cuisine;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.DayOfWeek;
import java.util.UUID;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
@Tag(name = "Restaurant", description = "Restaurant Endpoint")
public class RestaurantController {

    private final SaveDataRestaurantUseCase saveDataRestaurantUseCase;
    private final GetRestaurantUseCase getRestaurantUseCase;

    @Operation(summary = "Create a Restaurant")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "201", headers = {@Header(name = "/restaurant/id", description = "Location of restaurant")}),
            @ApiResponse(description = "Bad Request", responseCode = "400")
    })
    @PostMapping(
            path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> create(
            @RequestBody final RestaurantRequest request, UriComponentsBuilder uriComponentsBuilder) {

        var addressModel = new Restaurant.Address(
                request.address().street(),
                request.address().number(),
                request.address().addittionalDetails(),
                request.address().neighborhood(),
                request.address().city(),
                request.address().state()
        );

        var openinHours = request.opening_hour().stream()
                .map(openingHourRequest -> new
                        Restaurant.OpeningHour(
                            openingHourRequest.day_of_week(),
                            openingHourRequest.start(),
                            openingHourRequest.end()
                        )
                ).toList();

        var cuisines = request.cuisines().stream()
                .map(cuisineRequest -> new Restaurant.Cuisine(cuisineRequest.cuisine()))
                .toList();

        var restaurantModel = new Restaurant(
                request.name(), addressModel, openinHours, cuisines, request.tables()
        );

        var retaurantId = this.saveDataRestaurantUseCase.create(restaurantModel);

        URI location = uriComponentsBuilder
                .path("restaurants/{id}")
                .buildAndExpand(retaurantId.id())
                .toUri();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location.toASCIIString())
                .build();

    }

    @Operation(summary = "Get a restaurant by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantResponse.class)
                    )
            ),
            @ApiResponse(
                    description = "Bad request", responseCode = "400",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantError.class)
                    )
            ),
            @ApiResponse(
                    description = "Not found", responseCode = "404",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = RestaurantError.class)
                    )
            )
    })
    @GetMapping(path = "/{restaurantId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<RestaurantResponse> getById(@PathVariable final String restaurantId){

        var restaurantIdModel = new Restaurant.RestaurantId(restaurantId);

        var restaurantData = getRestaurantUseCase.findById(restaurantIdModel);

        var addressResponse = new AddressResponse(
                restaurantData.get().getAddress().getStreet(),
                restaurantData.get().getAddress().getNumber(),
                restaurantData.get().getAddress().getAddittionalDetails(),
                restaurantData.get().getAddress().getNeighborhood(),
                restaurantData.get().getAddress().getCity(),
                restaurantData.get().getAddress().getState()
        );

        var response = new RestaurantResponse(
                UUID.fromString(restaurantData.get().getRestaurantId().id()),
                restaurantData.get().getName(),
                addressResponse
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);


    }


}



