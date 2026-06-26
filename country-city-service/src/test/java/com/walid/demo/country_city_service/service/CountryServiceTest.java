package com.walid.demo.country_city_service.service;

import com.walid.demo.country_city_service.dto.CountryDto;
import com.walid.demo.country_city_service.model.Country;
import com.walid.demo.country_city_service.respository.CountryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryServiceTest {

    @Mock
    private CountryRepository countryRepository;

    @InjectMocks
    private CountryService countryService;

    // =========================
    // 1. EMPTY LIST
    // =========================
    @Test
    void shouldReturnEmptyList() {

        // Arrange
        when(countryRepository.findAll())
                .thenReturn(List.of());

        // Act
        List<CountryDto> result =
                countryService.getAllCountries();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =========================
    // 2. SINGLE COUNTRY
    // =========================
    @Test
    void shouldReturnSingleCountry() {

        // Arrange
        Country country = new Country(1L, "France");

        when(countryRepository.findAll())
                .thenReturn(List.of(country));

        // Act
        List<CountryDto> result =
                countryService.getAllCountries();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1L, result.get(0).id());
        assertEquals("France", result.get(0).name());
    }

    // =========================
    // 3. MULTIPLE COUNTRIES
    // =========================
    @Test
    void shouldReturnMultipleCountries() {

        // Arrange
        List<Country> countries = List.of(
                new Country(1L, "France"),
                new Country(2L, "Germany"),
                new Country(3L, "Spain")
        );

        when(countryRepository.findAll())
                .thenReturn(countries);

        // Act
        List<CountryDto> result =
                countryService.getAllCountries();

        // Assert
        assertEquals(3, result.size());

        assertEquals("France", result.get(0).name());
        assertEquals("Germany", result.get(1).name());
        assertEquals("Spain", result.get(2).name());
    }
}