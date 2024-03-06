package com.github.rafaelfernandes.restaurant.adapter.out.persistence;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "restaurant")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantJpaEntity {

    @Id
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "register")
    private LocalDateTime register;

    @Column(name = "tables")
    private Integer tables;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "restaurant")
    private List<OpeningHourJpaEntity> openingHours;

    @OneToMany(targetEntity = CuisineJpaEntity.class)
    private List<CuisineJpaEntity> cuisines;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "restaurant")
    private AddressJpaEntity address;



}
