package com.github.rafaelfernandes.service.adapter.in.web;

import com.github.rafaelfernandes.common.annotations.WebAdapter;
import com.github.rafaelfernandes.reservation.adapter.in.web.request.ReservationResponse;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.service.adapter.in.web.request.ServiceRequest;
import com.github.rafaelfernandes.service.adapter.in.web.response.OpeningHourServiceResponse;
import com.github.rafaelfernandes.service.adapter.in.web.response.ServiceResponse;
import com.github.rafaelfernandes.service.application.domain.model.Service;
import com.github.rafaelfernandes.service.application.port.in.ManageServiceUseCase;
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
import java.util.List;

@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/services")
@Tag(name = "02 - Service", description = "Service Hour Endpoint")
public class ServiceController {

    private final ManageServiceUseCase useCase;

    @Operation(summary = "Create a service")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "201", headers = {@Header(name = "/services/id", description = "Location of service")}),
            @ApiResponse(description = "Bad Request", responseCode = "400")
    })
    @PostMapping(
            path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> create(
            @Parameter @RequestBody final ServiceRequest request, UriComponentsBuilder uriComponentsBuilder
    ){

        var restaurauntId = new Restaurant.RestaurantId(request.retaurantId());

        var openingHourService = new Restaurant.OpeningHour(
                request.opening_hour().day_of_week(),
                request.opening_hour().start(),
                request.opening_hour().end()
        );

        var serviceSave = useCase.create(restaurauntId, openingHourService, request.date(), request.tables());

        URI location = uriComponentsBuilder
                .path("services/{id}")
                .buildAndExpand(serviceSave.id())
                .toUri();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header(HttpHeaders.LOCATION, location.toASCIIString())
                .build();

    }

    @Operation(summary = "Get a service by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ServiceResponse.class)
                    )
            ),
            @ApiResponse(
                    description = "Bad request", responseCode = "400"
            ),
            @ApiResponse(
                    description = "Not found", responseCode = "404"
            )
    })
    @GetMapping(path = "/{serviceId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ServiceResponse> getBydId(@PathVariable final String serviceId){
        var serviceIdModel = new Service.ServiceId(serviceId);

        var serviceData = useCase.details(serviceIdModel);

        var response = new ServiceResponse(
                serviceData.getServiceId().id(),
                serviceData.getRestaurantId().id(),
                new OpeningHourServiceResponse(
                        serviceData.getOpeningHour().getDayOfWeek(),
                        serviceData.getOpeningHour().getStart(),
                        serviceData.getOpeningHour().getEnd()
                ),
                serviceData.getDate(),
                serviceData.getTables()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(response);

    }

    @Operation(summary = "Update a service by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Success", responseCode = "200"
            ),
            @ApiResponse(
                    description = "Bad request", responseCode = "400"
            ),
            @ApiResponse(
                    description = "Not found", responseCode = "404"
            )
    })
    @PutMapping(path = "/{serviceId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> update(@PathVariable final String serviceId, @RequestBody ServiceRequest request){

        return null;

    }

    @Operation(summary = "Get all reservations by Service ID")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ReservationResponse.class)
                            )
                    )
            ),
            @ApiResponse(description = "Bad request", responseCode = "400"),
            @ApiResponse(description = "Not found", responseCode = "404")
    })
    @GetMapping(path = "/{serviceId}/reservations",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<List<ReservationResponse>> getAllReservationsByServiceId(@PathVariable final String serviceId){
        return null;
    }










}
