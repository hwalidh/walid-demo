package com.walid.demo.country_city_service.respository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.walid.demo.country_city_service.model.Country;

public interface CountryRepository extends JpaRepository<Country, Long> {
}
