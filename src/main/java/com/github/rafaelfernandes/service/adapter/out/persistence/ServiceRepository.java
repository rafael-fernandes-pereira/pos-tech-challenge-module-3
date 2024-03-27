package com.github.rafaelfernandes.service.adapter.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

public interface ServiceRepository extends JpaRepository<ServiceJpaEntity, UUID> {


    @Query("""
            SELECT s FROM ServiceJpaEntity s WHERE s.restaurantId = :restaurantId
            AND s.date = :date AND s.dayOfWeek = :dayOfWeek
            AND s.start <= :start AND s.end >= :end
            """)

    List<ServiceJpaEntity> findByCriteria(UUID restaurantId, LocalDate date, String dayOfWeek, LocalTime start, LocalTime end);
}
