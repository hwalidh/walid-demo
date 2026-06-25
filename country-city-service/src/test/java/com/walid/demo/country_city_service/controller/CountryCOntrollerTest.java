package com.walid.demo.country_city_service.controller;

import com.walid.demo.country_city_service.dto.CountryDto;
import com.walid.demo.country_city_service.service.CountryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

// 💡 IMPORT REQUIS POUR SIMULER LE JETON SÉCURISÉ
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CountryController.class)
class CountryControllerTest {

        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private CountryService service;

        // ==========================================
        // SECURITY
        // ==========================================
        @Test
        void shouldReturn401WhenNoTokenProvided() throws Exception {
                mockMvc.perform(get("/countries"))
                                .andExpect(status().isUnauthorized());
        }

        // =========================
        // 1. EMPTY LIST
        // =========================
        @Test
        void shouldReturnEmptyList() throws Exception {

                when(service.getAllCountries()).thenReturn(List.of());

                mockMvc.perform(get("/countries").with(jwt())) // 🔐 Ajout du token
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(0));

                verify(service).getAllCountries();
        }

        // =========================
        // 2. SINGLE COUNTRY
        // =========================
        @Test
        void shouldReturnSingleCountry() throws Exception {

                when(service.getAllCountries()).thenReturn(
                                List.of(new CountryDto(1L, "France")));

                mockMvc.perform(get("/countries").with(jwt())) // 🔐 Ajout du token
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(1))
                                .andExpect(jsonPath("$[0].id").value(1))
                                .andExpect(jsonPath("$[0].name").value("France"));

                verify(service).getAllCountries();
        }

        // =========================
        // 3. MULTIPLE COUNTRIES
        // =========================
        @Test
        void shouldReturnMultipleCountries() throws Exception {

                when(service.getAllCountries()).thenReturn(List.of(
                                new CountryDto(1L, "France"),
                                new CountryDto(2L, "Germany"),
                                new CountryDto(3L, "Spain")));

                mockMvc.perform(get("/countries").with(jwt())) // 🔐 Ajout du token
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.length()").value(3))
                                .andExpect(jsonPath("$[1].name").value("Germany"))
                                .andExpect(jsonPath("$[2].name").value("Spain"));

                verify(service).getAllCountries();
        }

        // =========================
        // 4. STRUCTURE VALIDATION
        // =========================
        @Test
        void shouldReturnValidJsonStructure() throws Exception {

                when(service.getAllCountries()).thenReturn(
                                List.of(new CountryDto(1L, "France")));

                mockMvc.perform(get("/countries").with(jwt())) // 🔐 Ajout du token
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].id").exists())
                                .andExpect(jsonPath("$[0].name").exists())
                                .andExpect(jsonPath("$[0].name").isString());
        }

        // =========================
        // 5. NOT EMPTY NAME VALIDATION (edge case)
        // =========================
        @Test
        void shouldNotReturnBlankCountryName() throws Exception {

                when(service.getAllCountries()).thenReturn(
                                List.of(new CountryDto(1L, "France")));

                mockMvc.perform(get("/countries").with(jwt())) // 🔐 Ajout du token
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$[0].name").isNotEmpty());
        }
}