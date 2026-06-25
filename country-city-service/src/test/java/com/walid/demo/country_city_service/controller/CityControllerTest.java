package com.walid.demo.country_city_service.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.walid.demo.country_city_service.dto.CityDto;
import com.walid.demo.country_city_service.exception.NotFoundException;
import com.walid.demo.country_city_service.service.CityService;

@WebMvcTest(CityController.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService service;

    // ==========================================
    // SÉCURITÉ
    // ==========================================
    @Test
    void shouldReturn401WhenNoTokenProvidedForCities() throws Exception {
        mockMvc.perform(get("/countries/1/cities"))
               .andExpect(status().isUnauthorized());
               
        mockMvc.perform(get("/cities/1"))
               .andExpect(status().isUnauthorized());
    }

    // ===== getCities() tests =====

    @Test
    void getCities_returnsPageOfCities() throws Exception {
        CityDto dto1 = new CityDto(1L, "Paris", 2100000, "Capital");
        CityDto dto2 = new CityDto(2L, "Lyon", 500000, "Second city");
        Page<CityDto> page = new PageImpl<>(List.of(dto1, dto2));

        when(service.getCitiesByCountry(1L, PageRequest.of(0, 10))).thenReturn(page);

        mockMvc.perform(get("/countries/1/cities")
                .param("page", "0")
                .param("size", "10")
                .with(jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content.length()").value(2))
               .andExpect(jsonPath("$.content[0].name").value("Paris"))
               .andExpect(jsonPath("$.content[1].name").value("Lyon"));
    }

    @Test
    void getCities_withCustomPageSize() throws Exception {
        CityDto dto = new CityDto(1L, "Paris", 2100000, "Capital");
        Page<CityDto> page = new PageImpl<>(List.of(dto));

        when(service.getCitiesByCountry(1L, PageRequest.of(0, 20))).thenReturn(page);

        mockMvc.perform(get("/countries/1/cities")
                .param("page", "0")
                .param("size", "20")
                .with(jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content.length()").value(1));

        verify(service).getCitiesByCountry(eq(1L), eq(PageRequest.of(0, 20)));
    }

    @Test
    void getCities_withDifferentPage() throws Exception {
        CityDto dto = new CityDto(3L, "Marseille", 880000, "Third city");
        Page<CityDto> page = new PageImpl<>(List.of(dto));

        when(service.getCitiesByCountry(1L, PageRequest.of(1, 10))).thenReturn(page);

        mockMvc.perform(get("/countries/1/cities")
                .param("page", "1")
                .param("size", "10")
                .with(jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content[0].name").value("Marseille"));
    }

    @Test
    void getCities_emptyResult() throws Exception {
        Page<CityDto> emptyPage = new PageImpl<>(Collections.emptyList());
        when(service.getCitiesByCountry(999L, PageRequest.of(0, 10))).thenReturn(emptyPage);

        mockMvc.perform(get("/countries/999/cities")
                .with(jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.content.length()").value(0));
    }

    // ===== getCity() tests =====

    @Test
    void getCity_found() throws Exception {
        CityDto dto = new CityDto(1L, "Paris", 2100000, "Capital");
        when(service.getCityById(1L)).thenReturn(dto);

        mockMvc.perform(get("/cities/1")
                .with(jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.id").value(1L))
               .andExpect(jsonPath("$.name").value("Paris"))
               .andExpect(jsonPath("$.population").value(2100000))
               .andExpect(jsonPath("$.description").value("Capital"));
    }

    @Test
    void getCity_notFound_throwsException() throws Exception {
        when(service.getCityById(999L)).thenThrow(new NotFoundException("City not found: 999"));

        mockMvc.perform(get("/cities/999")
                .with(jwt()))
               .andExpect(status().isNotFound()); // Géré par votre GlobalExceptionHandler
    }

    @Test
    void getCity_differentIds() throws Exception {
        CityDto dto2 = new CityDto(2L, "Lyon", 500000, "Second city");
        CityDto dto3 = new CityDto(3L, "Marseille", 880000, "Third city");

        when(service.getCityById(2L)).thenReturn(dto2);
        when(service.getCityById(3L)).thenReturn(dto3);

        mockMvc.perform(get("/cities/2").with(jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Lyon"));

        mockMvc.perform(get("/cities/3").with(jwt()))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.name").value("Marseille"));
    }
}