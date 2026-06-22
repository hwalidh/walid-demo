package com.walid.demo.country_city_service.config;

import com.walid.demo.country_city_service.model.City;
import com.walid.demo.country_city_service.model.Country;
import com.walid.demo.country_city_service.respository.CityRepository;
import com.walid.demo.country_city_service.respository.CountryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final CountryRepository countryRepository;
    private final CityRepository cityRepository;

    @Override
    public void run(String... args) {

        cityRepository.deleteAll();
        countryRepository.deleteAll();

        // 🌍 COUNTRIES

        Country france = countryRepository.save(new Country(null, "France"));
        Country germany = countryRepository.save(new Country(null, "Germany"));
        Country algeria = countryRepository.save(new Country(null, "Algeria"));
        Country switzerland = countryRepository.save(new Country(null, "Switzerland"));
        Country luxembourg = countryRepository.save(new Country(null, "Luxembourg"));
        Country india = countryRepository.save(new Country(null, "India"));
        Country japan = countryRepository.save(new Country(null, "Japan")); 

        // 🏙️ CITIES

        cityRepository.saveAll(List.of(

                // 🇫🇷 France (multiple cities)
                new City(null, "Paris", france.getId(), 2100000, "Capital"),
                new City(null, "Lyon", france.getId(), 500000, "City"),
                new City(null, "Marseille", france.getId(), 870000, "Port city"),

                // 🇩🇪 Germany (multiple cities)
                new City(null, "Berlin", germany.getId(), 3500000, "Capital"),
                new City(null, "Munich", germany.getId(), 1500000, "Economic hub"),
                new City(null, "Hamburg", germany.getId(), 1800000, "Port city"),

                // 🇩🇿 Algeria (multiple cities)
                new City(null, "Algiers", algeria.getId(), 2700000, "Capital"),
                new City(null, "Oran", algeria.getId(), 1000000, "Coastal city"),
                new City(null, "Annaba", algeria.getId(), 500000, "Industrial city"),
                new City(null, "Constantine", algeria.getId(), 450000, "Historic city"),

                // 🇨🇭 Switzerland (multiple cities)
                new City(null, "Zurich", switzerland.getId(), 430000, "Financial center"),
                new City(null, "Geneva", switzerland.getId(), 200000, "International city"),

                // 🇱🇺 Luxembourg (single city)
                new City(null, "Luxembourg City", luxembourg.getId(), 130000, "Capital"),

                // 🇮🇳 India (multiple cities)
                new City(null, "New Delhi", india.getId(), 30000000, "Capital"),
                new City(null, "Mumbai", india.getId(), 20000000, "Financial capital"),
                new City(null, "Bangalore", india.getId(), 13000000, "Tech hub")

                // 🇯🇵 Japan → no cities (test edge case)
        ));
    }
}
