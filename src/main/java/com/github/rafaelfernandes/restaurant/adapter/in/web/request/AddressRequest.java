package com.github.rafaelfernandes.restaurant.adapter.in.web.request;

public record AddressRequest(
        String street,

        Integer number,

        String addittionalDetails,

        String neighborhood,

        String city,

        String state
) {
}
