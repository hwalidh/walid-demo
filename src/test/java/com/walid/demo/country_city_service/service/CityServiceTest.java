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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

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

    @Test
    void shouldThrowNotFoundWhenCityMissing() {
        when(repo.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> service.getCityById(99L));
    }

    @Test
    void shouldReturnCitiesPage() {
        City c = new City(1L, "Paris", 1L, 2100000, "Capital");
        PageImpl<City> page = new PageImpl<>(List.of(c));
        Pageable pageable = PageRequest.of(0, 10);

        when(repo.findByCountryId(1L, pageable)).thenReturn(page);

        Page<CityDto> result = service.getCitiesByCountry(1L, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Paris", result.getContent().get(0).name());
    }
}
