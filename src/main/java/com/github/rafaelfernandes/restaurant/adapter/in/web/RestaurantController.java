package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.reservation.adapter.in.web.request.ReservationResponse;
import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.adapter.in.web.response.*;
import com.github.rafaelfernandes.restaurant.application.port.in.*;
import com.github.rafaelfernandes.common.annotations.WebAdapter;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.common.enums.Cuisine;
import com.github.rafaelfernandes.review.adapter.in.web.response.ReviewResponse;
import com.github.rafaelfernandes.service.adapter.in.web.response.ServiceResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.ArraySchema;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
@Tag(name = "01 - Restaurant", description = "Restaurant Endpoint")
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
            @Parameter @RequestBody final RestaurantRequest request, UriComponentsBuilder uriComponentsBuilder) {

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

        var response = getRestaurantResponse(restaurantData);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);
    }


    @Operation(summary = "Search Restaurant")
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
    @GetMapping(path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<RestaurantResponse>> getAllBy(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            @RequestParam(required = false) List<String> cuisines ){

        var cuisinesEnum = cuisines.stream()
                .map(Cuisine::valueOf)
                .toList();

        var restaurants = getRestaurantUseCase.findAllBy(name, location, cuisinesEnum);

        var restaurantsData = restaurants.stream()
                .map(RestaurantController::getRestaurantResponse)
                .toList();


        return ResponseEntity
                .status(HttpStatus.OK)
                .body(restaurantsData);



    }

    private static RestaurantResponse getRestaurantResponse(Restaurant restaurant){
        return getRestaurantResponse(Optional.ofNullable(restaurant));
    }

    private static RestaurantResponse getRestaurantResponse(Optional<Restaurant> restaurantData) {

        var addressResponse = new AddressResponse(
                restaurantData.get().getAddress().getStreet(),
                restaurantData.get().getAddress().getNumber(),
                restaurantData.get().getAddress().getAddittionalDetails(),
                restaurantData.get().getAddress().getNeighborhood(),
                restaurantData.get().getAddress().getCity(),
                restaurantData.get().getAddress().getState()
        );



        var openingHoursResponses = restaurantData.get().getOpeningHours().stream()
                .map(openingHour -> new OpeningHourResponse(
                        openingHour.getDayOfWeek(),
                        openingHour.getStart(),
                        openingHour.getEnd()
                ))
                .toList();

        var cuisineResponses = restaurantData.get().getCuisines().stream()
                .map(cuisine -> new CuisineResponse(cuisine.getCuisine()))
                .toList();

        return new RestaurantResponse(
                UUID.fromString(restaurantData.get().getRestaurantId().id()),
                restaurantData.get().getName(),
                addressResponse,
                restaurantData.get().getTables(),
                openingHoursResponses,
                cuisineResponses
        );

    }

    @Operation(summary = "Get all services by Restaurant ID and between dates")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ServiceResponse.class)
                            )
                    )
            ),
            @ApiResponse(description = "Bad request", responseCode = "400"),
            @ApiResponse(description = "Not found", responseCode = "404")
    })
    @GetMapping(path = "/{restaurantId}/services",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ServiceResponse>> getAllServicesByRestaurantIdAndBetweenDates(@PathVariable final String restaurantId, @RequestParam final LocalDate from, @RequestParam final LocalDate to){
        return null;
    }

    @Operation(summary = "Get all reviews by Restaurant ID")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ReviewResponse.class)
                            )
                    )
            ),
            @ApiResponse(description = "Bad request", responseCode = "400"),
            @ApiResponse(description = "Not found", responseCode = "404")
    })
    @GetMapping(path = "/{restaurantId}/reviews",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ReviewResponse>> getAllReviewsByServiceId(@PathVariable final String restaurantId){
        return null;
    }

}



