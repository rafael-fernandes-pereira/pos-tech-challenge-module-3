package com.github.rafaelfernandes.restaurant.application.domain.model;

import com.github.rafaelfernandes.restaurant.common.enums.State;
import com.github.rafaelfernandes.restaurant.common.validation.ValueOfEnum;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Value;
import org.hibernate.validator.constraints.Length;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.github.rafaelfernandes.restaurant.common.validation.Validation.validate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Restaurant {

    private final RestaurantId restaurantId;

    @NotEmpty(message = "O campo deve estar preenchido")
    @Length(min = 3, max = 100, message = "O campo deve ter no minimo 3 e no maximo 100 caracteres")
    private String name;

    @NotNull(message = "O campo deve estar preenchido")
    private Address address;

    private final LocalDateTime register;

    @NotNull(message = "O campo deve estar preenchido")
    private List<OpeningHour> openingHours;

    @NotNull(message = "O campo deve estar preenchido")
    private Integer tables;

    @NotNull(message = "O campo deve estar preenchido")
    private List<Cuisine> cuisines;

    public record RestaurantId(
            @org.hibernate.validator.constraints.UUID(message = "O campo deve ser do tipo UUID")
            String id
    ) {
        public RestaurantId(String id){
            this.id = id;
            validate(this);
        }
    }

    @Value
    public static class Address {

        @NotEmpty(message = "O campo deve estar preenchido")
        @Length( min = 10, max = 150, message = "O campo deve ter no minimo {min} e no maximo {max} caracteres")
        String street;

        @NotNull(message = "O campo deve estar preenchido")
        @Positive(message = "O campo deve ser maior que zero (0)")
        Integer number;

        @Length( max = 150, message = "O campo deve ter no máximo {max} caracteres")
        String addittionalDetails;

        @NotEmpty(message = "O campo deve estar preenchido")
        @Length( min = 3, max = 30, message = "O campo deve ter no minimo 3 e no máximo 30 caracteres")
        String neighborhood;

        @NotEmpty(message = "O campo deve estar preenchido")
        @Length( min = 3, max = 60, message = "O campo deve ter no minimo 3 e no máximo 60 caracteres")
        String city;

        @NotNull(message = "O campo deve estar preenchido")
        @ValueOfEnum(enumClass = State.class, message = "O campo deve ser uma sigla de estado válida")
        String state;

        public Address(String street, Integer number, String addittionalDetails, String neighborhood, String city, String state) {
            this.street = street;
            this.number = number;
            this.addittionalDetails = addittionalDetails;
            this.neighborhood = neighborhood;
            this.city = city;
            this.state = state;
            validate(this);
        }
    }

    @Value
    public static class OpeningHour {
        @NotNull(message = "O campo deve estar preenchido")
        @ValueOfEnum(enumClass = DayOfWeek.class, message = "O campo deve ser uma sigla de dias válidos")
        String dayOfWeek;

        @NotNull(message = "O campo deve estar preenchido")
        LocalTime start;

        @NotNull(message = "O campo deve estar preenchido")
        LocalTime end;
    }

    @Value
    public static class Cuisine {
        @NotEmpty(message = "O campo deve ser preenchido")
        @ValueOfEnum(enumClass = com.github.rafaelfernandes.restaurant.common.enums.Cuisine.class)
        String cuisine;
    }

    public Restaurant(String name, Address address, List<OpeningHour> openingHours, List<Cuisine> cuisines, Integer tables) {
        this.name = name;
        this.address = address;

        this.register = LocalDateTime.now();

        this.openingHours = openingHours;
        this.cuisines = cuisines;

        this.tables = tables;
        validate(this);

        this.restaurantId = new RestaurantId(UUID.randomUUID().toString());

    }

    public static Restaurant of(String restaurantId, String name, Address address, LocalDateTime register, List<OpeningHour> openingHours, Integer numberOfTables, List<Cuisine> cuisines){
        return new Restaurant(new RestaurantId(restaurantId), name, address, register, openingHours, numberOfTables, cuisines);
    }

}
