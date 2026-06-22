package com.walid.demo.country_city_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.walid.demo.country_city_service.dto.CityDto;
import com.walid.demo.country_city_service.exception.NotFoundException;
import com.walid.demo.country_city_service.model.City;
import com.walid.demo.country_city_service.respository.CityRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityRepository repo;

        public Page<CityDto> getCitiesByCountry(Long countryId, Pageable pageable) {
            return repo.findByCountryId(countryId, pageable)
                    .map(c -> new CityDto(
                            c.getId(),
                            c.getName(),
                            c.getPopulation(),
                            c.getDescription()
                    ));
        }

    public CityDto getCityById(Long id) {
        City city = repo.findById(id)
                .orElseThrow(() -> new NotFoundException("City not found: " + id));

        return new CityDto(
                city.getId(),
                city.getName(),
                city.getPopulation(),
                city.getDescription()
        );
    }
}