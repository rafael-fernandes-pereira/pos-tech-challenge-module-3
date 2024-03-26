package com.github.rafaelfernandes.service.adapter.out.persistence;

import com.github.rafaelfernandes.restaurant.application.domain.model.Restaurant;
import com.github.rafaelfernandes.service.application.domain.model.Service;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class ManageServiceAdapterTest {

    @Mock
    private ServiceRepository serviceRepository;

    @InjectMocks
    private ManageServiceAdapter manageServiceAdapter;

    public ManageServiceAdapterTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testExistsService_True() {
        // Arrange
        Restaurant.RestaurantId restaurantId = new Restaurant.RestaurantId(UUID.randomUUID().toString());
        Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour("Monday", LocalTime.of(9, 0), LocalTime.of(17, 0));
        LocalDate date = LocalDate.of(2024, 3, 25);

        var lista = new ArrayList<ServiceJpaEntity>();
        lista.add(Mockito.mock(ServiceJpaEntity.class));

        when(serviceRepository.findByCriteria(any(UUID.class), any(LocalDate.class), anyString(), any(LocalTime.class), any(LocalTime.class))).thenReturn(lista);

        // Act
        boolean exists = manageServiceAdapter.existsService(restaurantId, openingHour, date);

        // Assert
        assertTrue(exists);
        verify(serviceRepository, times(1)).findByCriteria(any(UUID.class), any(LocalDate.class), anyString(), any(LocalTime.class), any(LocalTime.class));
    }

    @Test
    public void testExistsService_False() {
        // Arrange
        Restaurant.RestaurantId restaurantId = new Restaurant.RestaurantId(UUID.randomUUID().toString());
        Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour("Monday", LocalTime.of(9, 0), LocalTime.of(17, 0));
        LocalDate date = LocalDate.of(2024, 3, 25);
        when(serviceRepository.findByCriteria(any(UUID.class), any(LocalDate.class), anyString(), any(LocalTime.class), any(LocalTime.class))).thenReturn(new ArrayList<>());

        // Act
        boolean exists = manageServiceAdapter.existsService(restaurantId, openingHour, date);

        // Assert
        assertFalse(exists);
        verify(serviceRepository, times(1)).findByCriteria(any(UUID.class), any(LocalDate.class), anyString(), any(LocalTime.class), any(LocalTime.class));
    }

    @Test
    public void testSave() {
        // Arrange
        Restaurant.RestaurantId restaurantId = Mockito.mock(Restaurant.RestaurantId.class);
        Restaurant.OpeningHour openingHour = new Restaurant.OpeningHour("Monday", LocalTime.of(8, 0), LocalTime.of(18, 0));
        LocalDate date = LocalDate.now();
        Integer tables = 10;

        // Mock para o repository save
        ServiceJpaEntity savedEntity = new ServiceJpaEntity(UUID.randomUUID(), UUID.randomUUID(), date, "Monday", LocalTime.of(8, 0), LocalTime.of(18, 0), tables);
        when(serviceRepository.save(any(ServiceJpaEntity.class))).thenReturn(savedEntity);

        // Act
        Service savedService = manageServiceAdapter.save(restaurantId, openingHour, date, tables);

        // Assert
        assertNotNull(savedService);
        assertNotNull(savedService.getServiceId());
        assertNotNull(savedService.getRestaurantId());
        assertNotNull(savedService.getOpeningHour());
        assertNotNull(savedService.getDate());
        assertNotNull(savedService.getTables());
        assertEquals(restaurantId.id(), savedService.getRestaurantId().id());
        assertEquals(openingHour.getDayOfWeek(), savedService.getOpeningHour().getDayOfWeek());
        assertEquals(openingHour.getStart(), savedService.getOpeningHour().getStart());
        assertEquals(openingHour.getEnd(), savedService.getOpeningHour().getEnd());
        assertEquals(date, savedService.getDate());
        assertEquals(tables, savedService.getTables());

        // Verificar se o método save do repository foi chamado
        verify(serviceRepository, times(1)).save(any(ServiceJpaEntity.class));
    }

}