package com.walid.demo.country_city_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.walid.demo.country_city_service.dto.CityDto;
import com.walid.demo.country_city_service.model.City;
import com.walid.demo.country_city_service.respository.CityRepository;

@ExtendWith(MockitoExtension.class)
class CityServiceTest {

    @Mock
    private CityRepository repo;

    @InjectMocks
    private CityService service;

    @Test
    void shouldReturnCityById() {
        when(repo.findById(1L))
                .thenReturn(Optional.of(new City(1L, "Paris", 1L, 1000, "desc")));

        CityDto result = service.getCityById(1L);

        assertEquals("Paris", result.name());
    }
}