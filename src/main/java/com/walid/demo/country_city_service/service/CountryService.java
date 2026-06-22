package com.walid.demo.country_city_service.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.walid.demo.country_city_service.dto.CountryDto;
import com.walid.demo.country_city_service.respository.CountryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository repo;

    public List<CountryDto> getAllCountries() {
        return repo.findAll()
                .stream()
                .map(c -> new CountryDto(c.getId(), c.getName()))
                .toList();
    }
}
