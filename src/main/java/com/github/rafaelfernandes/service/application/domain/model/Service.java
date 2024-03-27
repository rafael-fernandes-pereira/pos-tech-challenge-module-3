package com.github.rafaelfernandes.service.application.domain.model;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import jakarta.validation.ValidationException;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
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
    private final ServiceId serviceId;

    @NotNull(message = "O campo deve estar preenchido")
    private Restaurant.RestaurantId restaurantId;

    @NotNull(message = "O campo deve estar preenchido")
    private Restaurant.OpeningHour openingHour;

    @NotNull(message = "A data do serviço dever ser hoje ou futura")
    @FutureOrPresent(message = "A data do serviço dever ser hoje ou futura")
    private LocalDate date;

    @NotNull(message = "O mínimo de mesas é 1")
    @Min(value = 1, message = "O mínimo de mesas é {value}")
    private Integer tables;

    public record ServiceId(
            @NotEmpty(message = "O campo deve ser do tipo UUID")
            @org.hibernate.validator.constraints.UUID(message = "O campo deve ser do tipo UUID")
            String id
    ) {
        public ServiceId(String id){
            this.id = id;
            validate(this);
        }
    }

    public Service(String serviceId, Restaurant.RestaurantId restaurantId, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables) {
        this.serviceId = new ServiceId(serviceId);
        this.restaurantId = restaurantId;
        this.openingHour = openingHour;
        this.date = date;
        this.tables = tables;
        validate(this);
    }

    public Service(Restaurant restaurant, Restaurant.OpeningHour openingHour, LocalDate date, Integer tables) {

        if (restaurant == null) throw new ValidationException("restaurant: O campo deve estar preenchido");

        this.restaurantId = restaurant.getRestaurantId();
        this.openingHour = openingHour;
        this.date = date;
        this.tables = tables;
        validate(this);

        var validOpeningHour = restaurant.getOpeningHours().stream()
                .anyMatch(openingHourRestaurant ->
                        openingHourRestaurant.getDayOfWeek().equals(openingHour.getDayOfWeek()) &&
                                (openingHour.getStart().equals(openingHourRestaurant.getStart()) || openingHour.getStart().isAfter(openingHourRestaurant.getStart())) &&
                                (openingHour.getEnd().equals(openingHourRestaurant.getEnd()) || openingHour.getEnd().isBefore(openingHourRestaurant.getEnd())));



        if (!validOpeningHour) throw new IllegalArgumentException("openingHour: não existe esse horário no restaurante enviado.");

        if (tables > restaurant.getTables()) throw new IllegalArgumentException("tables: o número de mesas para o serviço é maior que a cadastrada no restaurante");

        this.serviceId = new ServiceId(UUID.randomUUID().toString());
    }
}
