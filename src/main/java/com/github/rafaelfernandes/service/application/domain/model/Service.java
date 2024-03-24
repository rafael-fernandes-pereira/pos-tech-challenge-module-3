package com.github.rafaelfernandes.service.application.domain.model;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;
import java.util.UUID;

import static com.github.rafaelfernandes.common.validation.Validation.validate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Service {
    private final ReservationId reservationId;

    @NotNull
    private Restaurant.RestaurantId restaurantId;

    @NotNull
    private Restaurant.OpeningHour openingHour;

    @NotNull
    private LocalDate date;

    @NotNull(message = "O campo deve estar preenchido")
    @Min(value = 1, message = "O mínimo de mesas é {value}")
    private Integer tables;

    public record ReservationId(
            @org.hibernate.validator.constraints.UUID(message = "O campo deve ser do tipo UUID")
            String id
    ) {
        public ReservationId(String id){
            this.id = id;
            validate(this);
        }
    }

    public Service(String reservationId, Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables) {
        this.reservationId = new ReservationId(reservationId);
        this.restaurantId = restaurantId;
        this.openingHour = openingHour;
        this.date = date;
        this.tables = tables;
        validate(this);
    }

    public Service(Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables){
        this.restaurantId = restaurantId;
        this.openingHour = openingHour;
        this.date = date;
        this.tables = tables;
        validate(this);
        this.reservationId = new ReservationId(UUID.randomUUID().toString());

    }
}
