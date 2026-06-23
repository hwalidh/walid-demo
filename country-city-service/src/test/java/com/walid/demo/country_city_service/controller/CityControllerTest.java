package com.walid.demo.country_city_service.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import com.walid.demo.country_city_service.dto.CityDto;
import com.walid.demo.country_city_service.exception.NotFoundException;
import com.walid.demo.country_city_service.service.CityService;

@ExtendWith(MockitoExtension.class)
class CityControllerTest {

    @Mock
    private CityService service;

    @InjectMocks
    private CityController controller;

    // ===== getCities() tests =====

    @Test
    void getCities_returnsPageOfCities() {
        // Arrange
        CityDto dto1 = new CityDto(1L, "Paris", 2100000, "Capital");
        CityDto dto2 = new CityDto(2L, "Lyon", 500000, "Second city");
        Page<CityDto> page = new PageImpl<>(List.of(dto1, dto2));

        when(service.getCitiesByCountry(1L, PageRequest.of(0, 10))).thenReturn(page);

        // Act
        Page<CityDto> result = controller.getCities(1L, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals("Paris", result.getContent().get(0).name());
        assertEquals("Lyon", result.getContent().get(1).name());
    }

    @Test
    void getCities_withCustomPageSize() {
        // Arrange
        CityDto dto = new CityDto(1L, "Paris", 2100000, "Capital");
        Page<CityDto> page = new PageImpl<>(List.of(dto));

        when(service.getCitiesByCountry(1L, PageRequest.of(0, 20))).thenReturn(page);

        // Act
        Page<CityDto> result = controller.getCities(1L, 0, 20);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        verify(service).getCitiesByCountry(eq(1L), eq(PageRequest.of(0, 20)));
    }

    @Test
    void getCities_withDifferentPage() {
        // Arrange
        CityDto dto = new CityDto(3L, "Marseille", 880000, "Third city");
        Page<CityDto> page = new PageImpl<>(List.of(dto));

        when(service.getCitiesByCountry(1L, PageRequest.of(1, 10))).thenReturn(page);

        // Act
        Page<CityDto> result = controller.getCities(1L, 1, 10);

        // Assert
        assertNotNull(result);
        assertEquals("Marseille", result.getContent().get(0).name());
    }

    @Test
    void getCities_emptyResult() {
        // Arrange
        Page<CityDto> emptyPage = new PageImpl<>(Collections.emptyList());
        when(service.getCitiesByCountry(999L, PageRequest.of(0, 10))).thenReturn(emptyPage);

        // Act
        Page<CityDto> result = controller.getCities(999L, 0, 10);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getContent().size());
    }

    // ===== getCity() tests =====

    @Test
    void getCity_found() {
        // Arrange
        CityDto dto = new CityDto(1L, "Paris", 2100000, "Capital");
        when(service.getCityById(1L)).thenReturn(dto);

        // Act
        CityDto result = controller.getCity(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.id());
        assertEquals("Paris", result.name());
        assertEquals(2100000, result.population());
        assertEquals("Capital", result.description());
    }

    @Test
    void getCity_notFound_throwsException() {
        // Arrange
        when(service.getCityById(999L)).thenThrow(new NotFoundException("City not found: 999"));

        // Act & Assert
        assertThrows(NotFoundException.class, () -> {
            controller.getCity(999L);
        });
    }

    @Test
    void getCity_verifyServiceCalled() {
        // Arrange
        CityDto dto = new CityDto(1L, "Paris", 2100000, "Capital");
        when(service.getCityById(1L)).thenReturn(dto);

        // Act
        controller.getCity(1L);

        // Assert
        verify(service).getCityById(1L);
    }

    @Test
    void getCity_differentIds() {
        // Arrange
        CityDto dto2 = new CityDto(2L, "Lyon", 500000, "Second city");
        CityDto dto3 = new CityDto(3L, "Marseille", 880000, "Third city");

        when(service.getCityById(2L)).thenReturn(dto2);
        when(service.getCityById(3L)).thenReturn(dto3);

        // Act
        CityDto result2 = controller.getCity(2L);
        CityDto result3 = controller.getCity(3L);

        // Assert
        assertEquals("Lyon", result2.name());
        assertEquals("Marseille", result3.name());
    }
}