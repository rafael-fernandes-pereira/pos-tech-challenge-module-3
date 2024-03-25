package com.github.rafaelfernandes.reservation.adapter.in.web;

import com.github.rafaelfernandes.common.annotations.WebAdapter;
import com.github.rafaelfernandes.reservation.adapter.in.web.request.ReservationRequest;
import com.github.rafaelfernandes.reservation.adapter.in.web.request.ReservationResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;


@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
@Tag(name = "03 - Reservations", description = "Reservations Endpoint")
public class ReservationController {

    @Operation(summary = "Create a Reservation")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "201", headers = {@Header(name = "/reservation/id", description = "Location of reservation")}),
            @ApiResponse(description = "Bad Request", responseCode = "400")
    })
    @PostMapping(
            path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> create(@Parameter @RequestBody final ReservationRequest request, UriComponentsBuilder uriComponentsBuilder) {
        return null;
    }

    @Operation(summary = "Get a reservation by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReservationResponse.class)
                    )
            ),
            @ApiResponse(description = "Bad request", responseCode = "400",
                    content = @Content(
                            schema = @Schema(implementation = Void.class)
                    )
            ),
            @ApiResponse(description = "Not found", responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = Void.class)
                    )
            )
    })
    @GetMapping(path = "/{reservationId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ReservationResponse> getBydId(@PathVariable final String reservationId){
        return null;
    }

    @Operation(summary = "Get a reservation by CODE")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReservationResponse.class)
                    )
            ),
            @ApiResponse(description = "Bad request", responseCode = "400",
                    content = @Content(
                            schema = @Schema(implementation = Void.class)
                    )
            ),
            @ApiResponse(description = "Not found", responseCode = "404",
                    content = @Content(
                            schema = @Schema(implementation = Void.class)
                    )
            ),
    })
    @GetMapping(path = "/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ReservationResponse> getByCode(@PathVariable final String code){
        return null;
    }

    @Operation(summary = "Delete reservation byId")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Bad request", responseCode = "400"),
            @ApiResponse(description = "Not found", responseCode = "404")
    })
    @DeleteMapping(path = "/{reservationId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> delete(@PathVariable final String reservationId){
        return null;
    }

    @Operation(summary = "Use reservation by Id")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Bad request", responseCode = "400"),
            @ApiResponse(description = "Not found", responseCode = "404")
    })
    @PutMapping(path = "/{reservationId}/use",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> use(@PathVariable final String reservationId){
        return null;
    }
















}
