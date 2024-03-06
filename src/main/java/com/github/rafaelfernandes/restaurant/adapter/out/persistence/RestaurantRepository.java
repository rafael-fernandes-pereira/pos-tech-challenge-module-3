package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<RestaurantJpaEntity, UUID> {
}
