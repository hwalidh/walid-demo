package com.walid.demo.country_city_service.config;

import org.springframework.stereotype.Component;

import com.walid.demo.country_city_service.model.City;
import com.walid.demo.country_city_service.model.Country;
import com.walid.demo.country_city_service.respository.CityRepository;
import com.walid.demo.country_city_service.respository.CountryRepository;

import jakarta.annotation.PostConstruct;
@Component
public class DataInitializer {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    public DataInitializer(CountryRepository countryRepository,
            CityRepository cityRepository) {
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
    }

    @PostConstruct
    public void init() {

        // ===================== COUNTRIES =====================
        Country france = new Country();
        france.setName("France");
        france = countryRepository.save(france);

        Country algeria = new Country();
        algeria.setName("Algeria");
        algeria = countryRepository.save(algeria);

        Country india = new Country();
        india.setName("India");
        india = countryRepository.save(india);

        Country luxembourg = new Country();
        luxembourg.setName("Luxembourg");
        luxembourg = countryRepository.save(luxembourg);

        Country japan = new Country();
        japan.setName("Japan");
        japan = countryRepository.save(japan);

        // ===================== FRANCE =====================
        cityRepository.save(newCity("Paris", 2140000, "Capital", france));
        cityRepository.save(newCity("Marseille", 870000, "Port city", france));
        cityRepository.save(newCity("Lille", 235000, "North city", france));
        cityRepository.save(newCity("Strasbourg", 280000, "European city", france));

        // ===================== ALGERIA =====================
        cityRepository.save(newCity("Annaba", 500000, "Coastal city", algeria));
        cityRepository.save(newCity("Oran", 1000000, "Second largest city", algeria));
        cityRepository.save(newCity("Constantine", 450000, "Historic city", algeria));
        cityRepository.save(newCity("Djanet", 17000, "Desert city", algeria));

        // ===================== INDIA =====================
        cityRepository.save(newCity("Mumbai", 12400000, "Financial capital", india));
        cityRepository.save(newCity("Delhi", 19000000, "Capital territory", india));
        cityRepository.save(newCity("Bangalore", 8400000, "Tech city", india));
        cityRepository.save(newCity("Hyderabad", 6800000, "IT hub", india));
        cityRepository.save(newCity("Chennai", 7200000, "Industrial city", india));
        cityRepository.save(newCity("Kolkata", 4500000, "Cultural city", india));

        // ===================== LUXEMBOURG =====================
        cityRepository.save(newCity("Luxembourg City", 130000, "Capital", luxembourg));
        cityRepository.save(newCity("Esch-sur-Alzette", 36000, "Industrial city", luxembourg));

        // ===================== JAPAN =====================
        cityRepository.save(newCity("Tokyo", 14000000, "Capital metropolis", japan));
    }

    private City newCity(String name, Integer population, String description, Country country) {
        City city = new City();
        city.setName(name);
        city.setPopulation(population);
        city.setDescription(description);
        city.setCountryId(country.getId());
        return city;
    }
}