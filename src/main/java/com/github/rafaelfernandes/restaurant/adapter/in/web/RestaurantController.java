package com.github.rafaelfernandes.restaurant.adapter.in.web;

import com.github.rafaelfernandes.restaurant.adapter.in.web.request.RestaurantRequest;
import com.github.rafaelfernandes.restaurant.application.port.in.CreateRestaurantAddressCommand;
import com.github.rafaelfernandes.restaurant.application.port.in.CreateRestaurantCommand;
import com.github.rafaelfernandes.restaurant.application.port.in.SaveDataRestaurantUseCase;
import com.github.rafaelfernandes.restaurant.common.annotations.WebAdapter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/restaurants")
@Tag(name = "Restaurant", description = "Restaurant Endpoint")
public class RestaurantController {

    private final SaveDataRestaurantUseCase saveDataRestaurantUseCase;

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

        var addressCommand = new CreateRestaurantAddressCommand(
                request.address().street(),
                request.address().number(),
                request.address().addittionalDetails(),
                request.address().neighborhood(),
                request.address().city(),
                request.address().state()
        );

        var command = new CreateRestaurantCommand(
                request.name(),
                addressCommand
        );

        var retaurantId = this.saveDataRestaurantUseCase.create(command);

        URI location = uriComponentsBuilder
                .path("restaurants/{id}")
                .buildAndExpand(retaurantId.getValue())
                .toUri();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location.toASCIIString())
                .build();

    }


}



