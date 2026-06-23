package com.walid.demo.country_city_service.controller;

import com.walid.demo.country_city_service.dto.CityDto;
import com.walid.demo.country_city_service.exception.NotFoundException;
import com.walid.demo.country_city_service.service.CityService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CityController.class)
class CityControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CityService service;

    @Test
    void getCitiesByCountry_returnsPage() throws Exception {
        CityDto dto = new CityDto(1L, "Paris", 2100000, "Capital");
        PageImpl<CityDto> page = new PageImpl<>(List.of(dto));

        when(service.getCitiesByCountry(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/countries/1/cities?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.content[0].name").value("Paris"));
    }

    @Test
    void getCitiesByCountry_defaultPagination_returnsPage() throws Exception {
        CityDto dto = new CityDto(1L, "Paris", 2100000, "Capital");
        PageImpl<CityDto> page = new PageImpl<>(List.of(dto));

        when(service.getCitiesByCountry(eq(1L), any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/countries/1/cities")) // no page/size params
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Paris"));
    }

    @Test
    void getCityById_found() throws Exception {
        CityDto dto = new CityDto(1L, "Paris", 2100000, "Capital");
        when(service.getCityById(1L)).thenReturn(dto);

        mockMvc.perform(get("/cities/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Paris"));
    }

    @Test
    void getCityById_notFound_returns404() throws Exception {
        when(service.getCityById(2L)).thenThrow(new NotFoundException("City not found: 2"));

        mockMvc.perform(get("/cities/2"))
                .andExpect(status().isNotFound());
    }
}
