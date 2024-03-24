package com.github.rafaelfernandes.restaurant.application.port.in;

import com.github.rafaelfernandes.common.validation.Validation;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.UUID;

import java.util.List;

@Data
public class GetRestarauntDataCommand {

    @UUID(message = "O campo deve ser do tipo UUID")
    private String restaurantId;

    @Length(min = 3, max = 100, message = "O campo deve ter no minimo 3 e no maximo 100 caracteres")
    private String name;

    private String location;

    private List<String> cuisines;

    @Positive(message = "O campo deve ser maior que zero (0)")
    private Integer page;

    @Positive(message = "O campo deve ser maior que zero (0)")
    private Integer quantity;

    @Pattern(regexp = "(ASC|DESC)", message = "O campo deve ser ASC ou DESC")
    private String orderBy;

    public GetRestarauntDataCommand(String restaurantId){
        this.restaurantId = restaurantId;
        Validation.validate(this);
    }

    public GetRestarauntDataCommand(String name, String location, List<String> cuisines, Integer page, Integer quantity, String orderBy) {
        this.name = name;
        this.location = location;
        this.cuisines = cuisines;
        this.page = page;
        this.quantity = quantity;
        this.orderBy = orderBy;
        Validation.validate(this);
    }
}
