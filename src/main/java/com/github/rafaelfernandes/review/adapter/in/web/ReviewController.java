package com.github.rafaelfernandes.review.adapter.in.web;

import com.github.rafaelfernandes.common.annotations.WebAdapter;
import com.github.rafaelfernandes.reservation.adapter.in.web.request.ReservationResponse;
import com.github.rafaelfernandes.review.adapter.in.web.request.ReviewRequest;
import com.github.rafaelfernandes.review.adapter.in.web.response.ReviewResponse;
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
@RequestMapping("/reviews")
@Tag(name = "04 - Reviews", description = "Reviews Endpoint")
public class ReviewController {

    @Operation(summary = "Create a Review")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "201", headers = {@Header(name = "/review/id", description = "Location of reservation")}),
            @ApiResponse(description = "Bad Request", responseCode = "400")
    })
    @PostMapping(
            path = "/",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> create(@Parameter @RequestBody final ReviewRequest request, UriComponentsBuilder uriComponentsBuilder) {
        return null;
    }

    @Operation(summary = "Get a review by ID")
    @ApiResponses(value = {
            @ApiResponse(
                    description = "Success", responseCode = "200",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ReviewResponse.class)
                    )
            ),
            @ApiResponse(description = "Bad request", responseCode = "400"),
            @ApiResponse(description = "Not found", responseCode = "404")
    })
    @GetMapping(path = "/{reviewId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<ReviewResponse> getBydId(@PathVariable final String reviewId){
        return null;
    }

    @Operation(summary = "Delete review by Id")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Bad request", responseCode = "400"),
            @ApiResponse(description = "Not found", responseCode = "404")
    })
    @DeleteMapping(path = "/{reviewId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> delete(@PathVariable final String reviewId){
        return null;
    }

    @Operation(summary = "Update review by Id")
    @ApiResponses(value = {
            @ApiResponse(description = "Success", responseCode = "200"),
            @ApiResponse(description = "Bad request", responseCode = "400"),
            @ApiResponse(description = "Not found", responseCode = "404")
    })
    @PutMapping(path = "/{reviewId}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Void> update(@PathVariable final String reviewId){
        return null;
    }




}
