package com.walid.demo.country_city_service.service;

import com.walid.demo.country_city_service.dto.CityDto;
import com.walid.demo.country_city_service.exception.NotFoundException;
import com.walid.demo.country_city_service.model.City;
import com.walid.demo.country_city_service.respository.CityRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository cityRepository;

    @InjectMocks
    private CityService cityService;

    // =========================
    // 1. GET CITY BY ID
    // =========================
    @Test
    void shouldReturnCityById() {

        // Arrange
        City city = new City(1L, "Paris", 1L, 2100000, "Capital");

        when(cityRepository.findById(1L))
                .thenReturn(Optional.of(city));

        // Act
        CityDto result = cityService.getCityById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Paris", result.name());
    }

    // =========================
    // 2. CITY NOT FOUND
    // =========================
    @Test
    void shouldThrowNotFoundWhenCityMissing() {

        // Arrange
        when(cityRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(
                NotFoundException.class,
                () -> cityService.getCityById(99L)
        );
    }

    // =========================
    // 3. GET CITIES BY COUNTRY (PAGINATION)
    // =========================
    @Test
    void shouldReturnCitiesPage() {

        // Arrange
        City city = new City(1L, "Paris", 1L, 2100000, "Capital");

        Pageable pageable = PageRequest.of(0, 10);
        Page<City> page = new PageImpl<>(List.of(city), pageable, 1);

        when(cityRepository.findByCountryId(1L, pageable))
                .thenReturn(page);

        // Act
        Page<CityDto> result =
                cityService.getCitiesByCountry(1L, pageable);

        // Assert
        assertEquals(1, result.getTotalElements());
        assertEquals("Paris", result.getContent().get(0).name());
        assertEquals(2100000, result.getContent().get(0).population());
    }
}