package ar.edu.utn.frc.tup.lciii.service;

import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.RequestPost;
import ar.edu.utn.frc.tup.lciii.entities.CountryEntity;
import ar.edu.utn.frc.tup.lciii.model.Country;
import ar.edu.utn.frc.tup.lciii.repository.CountryRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.Converters;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CountryService {

        private final CountryRepository countryRepository;

        private final RestTemplate restTemplate;


        public List<Country> getAllCountries() {
                String url = "https://restcountries.com/v3.1/all";
                List<Map<String, Object>> response = restTemplate.getForObject(url, List.class);
                return response.stream().map(this::mapToCountry).collect(Collectors.toList());
        }
        /**
         * Agregar mapeo de campo cca3 (String)
         * Agregar mapeo campos borders ((List<String>))
         */
        private Country mapToCountry(Map<String, Object> countryData) {
                Map<String, Object> nameData = (Map<String, Object>) countryData.get("name");
                return Country.builder()
                        .name((String) nameData.get("common"))
                        .population(((Number) countryData.get("population")).longValue())
                        .area(((Number) countryData.get("area")).doubleValue())
                        .region((String) countryData.get("region"))
                        .code((String) countryData.get("cca3"))
                        .languages((Map<String, String>) countryData.get("languages"))
                        .borders((List<String>) countryData.get("borders"))
                        .build();
        }


        private CountryDTO mapToDTO(Country country) {
                return new CountryDTO(country.getCode(), country.getName());
        }
        public List<CountryDTO> getAllCountriesDTOService() {
                List<CountryDTO> countriesDTO = new ArrayList<>();
                List<Country> lstcountry = getAllCountries();
                for (Country country : lstcountry) {
                        countriesDTO.add(mapToDTO(country));
                }
                return countriesDTO;
        }
        public List<CountryDTO> getCountriesDTOByNameService(String name) {
                List<CountryDTO> countriesDTO = new ArrayList<>();
                List<Country> lstcountry = getAllCountries();
                for (Country country : lstcountry) {
                        if(country.getName().equals(name) || country.getCode().equals(name)) {
                                countriesDTO.add(mapToDTO(country));
                        }

                }
                return countriesDTO;
        }
        public List<CountryDTO> getcontinentBylenguage(String languages){
                List<CountryDTO> countriesDTO = new ArrayList<>();
                List<Country> lstcountry = getAllCountries();
                for (Country country : lstcountry) {
                        if(country.getLanguages()!=null){
                                if (country.getLanguages().containsValue(languages)) {
                                        countriesDTO.add(mapToDTO(country));
                                }
                        }
                }
                return countriesDTO;

        }
        public List<CountryDTO> getcontinentByContinent(String continent){
                List<CountryDTO> countriesDTO = new ArrayList<>();
                List<Country> lstcountry = getAllCountries();
                for (Country country : lstcountry) {
                        if(country.getRegion()!=null){
                                if (country.getRegion().equals(continent)) {
                                        countriesDTO.add(mapToDTO(country));
                                }
                        }
                }
                return countriesDTO;
        }
        public List<CountryDTO> getCountryMoreBorders(){
                List<CountryDTO> countriesDTO = new ArrayList<>();
                List<Country> lstcountry = getAllCountries();
                Country countrySelected = new Country();
                int maxborders = 0;
                for (Country country : lstcountry) {
                        if(country.getBorders()!=null){
                                if(country.getBorders().size() > maxborders){
                                        countrySelected = country;
                                        maxborders = country.getBorders().size();
                                }
                        }

                }
                countriesDTO.add(mapToDTO(countrySelected));
                return countriesDTO;
        }
        public List<CountryDTO> postCountryAmount(RequestPost requestPost){

                List<CountryDTO> countriesDTO = new ArrayList<>();
                List<Country> lstcountry = getAllCountries();
                for (int i = 0; i <requestPost.getAmountOfCountryToSave()-1 ; i++) {
                        Country country = lstcountry.get(i);
                        CountryEntity countryEntity = new CountryEntity();
                        countryEntity.setCode(country.getCode());
                        countryEntity.setName(country.getName());
                        countryEntity.setPopulation(country.getPopulation());
                        countryEntity.setArea(country.getArea());
                        countryRepository.saveAndFlush(countryEntity);
                        countriesDTO.add(mapToDTO(lstcountry.get(i)));
                }
                Collections.shuffle(countriesDTO);

                return countriesDTO;

        }
}