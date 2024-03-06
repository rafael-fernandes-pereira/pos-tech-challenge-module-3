package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "cuisine")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CuisineJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "cusine")
    private String cusine;


}
