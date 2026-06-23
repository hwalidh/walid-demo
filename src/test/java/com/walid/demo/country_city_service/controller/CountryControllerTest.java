package com.walid.demo.country_city_service.controller;

import com.walid.demo.country_city_service.dto.CountryDto;
import com.walid.demo.country_city_service.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CountryService service;

    @Test
    void getCountries_returnsEmptyList() throws Exception {
        when(service.getAllCountries()).thenReturn(List.of());

        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0)); // empty array
    }

    @Test
    void getCountries_returnsSingleCountry() throws Exception {
        when(service.getAllCountries()).thenReturn(List.of(new CountryDto(1L, "France")));

        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("France"));
    }

    @Test
    void getCountries_returnsMultipleCountries() throws Exception {
        when(service.getAllCountries()).thenReturn(List.of(
                new CountryDto(1L, "France"),
                new CountryDto(2L, "Germany"),
                new CountryDto(3L, "Spain")
        ));

        mockMvc.perform(get("/countries"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[1].name").value("Germany"));
    }
}
