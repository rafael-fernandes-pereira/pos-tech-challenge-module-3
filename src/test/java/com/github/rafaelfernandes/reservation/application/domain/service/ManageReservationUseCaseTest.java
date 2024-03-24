package com.github.rafaelfernandes.reservation.application.domain.service;

import com.github.rafaelfernandes.common.exception.ReservationDuplicateException;
import com.github.rafaelfernandes.reservation.application.domain.model.Reservation;
import com.github.rafaelfernandes.reservation.application.port.out.ManageReservationPort;
import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

public class ManageReservationUseCaseTest {

    @Nested
    class Create {

        @Test
        public void testCreate() {
            // Criação de mocks
            ManageReservationPort manageReservationPort = Mockito.mock(ManageReservationPort.class);
            Restaurant restaurant = Mockito.mock(Restaurant.class);
            Restaurant.RestaurantId restaurantId = Mockito.mock(Restaurant.RestaurantId.class);
            Restaurant.OpeningHour openingHour = Mockito.mock(Restaurant.OpeningHour.class);
            LocalDate date = LocalDate.now();
            Integer tables = 1;

            // Configuração do mock
            Mockito.when(manageReservationPort.existsReservation(any(Restaurant.RestaurantId.class), any(Restaurant.OpeningHour.class), any(LocalDate.class))).thenReturn(false);
            Mockito.when(manageReservationPort.save(any(Restaurant.class), any(Restaurant.OpeningHour.class), any(LocalDate.class), any(Integer.class))).thenReturn(new Reservation("123e4567-e89b-12d3-a456-426614174000", restaurantId, openingHour, date, tables));

            // Criação do objeto a ser testado
            ManageReservationUseCase manageReservationUseCase = new ManageReservationUseCase(manageReservationPort);

            // Execução do método a ser testado
            Reservation.ReservationId reservationId = manageReservationUseCase.create(restaurant, openingHour, date, tables);

            // Verificação dos resultados
            assertNotNull(reservationId);
            assertEquals("123e4567-e89b-12d3-a456-426614174000", reservationId.id());
        }

        @Test
        public void testCreateWithExistingReservation() {
            // Criação de mocks
            ManageReservationPort manageReservationPort = Mockito.mock(ManageReservationPort.class);
            Restaurant restaurant = Mockito.mock(Restaurant.class);
            Restaurant.OpeningHour openingHour = Mockito.mock(Restaurant.OpeningHour.class);
            LocalDate date = LocalDate.now();
            Integer tables = 1;

            // Configuração do mock
            Mockito.when(manageReservationPort.existsReservation(restaurant.getRestaurantId(), openingHour, date)).thenReturn(true);

            // Criação do objeto a ser testado
            ManageReservationUseCase manageReservationUseCase = new ManageReservationUseCase(manageReservationPort);

            // Execução do método a ser testado e verificação da exceção
            assertThrows(ReservationDuplicateException.class, () -> {
                manageReservationUseCase.create(restaurant, openingHour, date, tables);
            });
        }

    }



}
