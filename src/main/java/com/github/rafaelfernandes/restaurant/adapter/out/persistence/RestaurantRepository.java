package com.github.rafaelfernandes.restaurant.adapter.out.persistence;

import com.github.rafaelfernandes.common.enums.Cuisine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface RestaurantRepository extends JpaRepository<RestaurantJpaEntity, UUID> {

    boolean existsByName(String name);

    @Query("""
            SELECT r FROM RestaurantJpaEntity r
            WHERE (:name IS NULL OR r.fullSearch LIKE %:name%)
            AND (:location IS NULL OR r.fullSearch LIKE %:location%)
            AND (:cuisineList IS NULL OR r.fullSearch IN :cuisineList)
            """)
    List<RestaurantJpaEntity> findRestaurantsByCriteria(@Param("name") String name,
                                                        @Param("location") String location,
                                                        @Param("cuisineList") List<Cuisine> cuisineList);

}
