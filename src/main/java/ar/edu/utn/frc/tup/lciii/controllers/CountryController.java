package ar.edu.utn.frc.tup.lciii.controllers;
import ar.edu.utn.frc.tup.lciii.dtos.common.CountryDTO;
import ar.edu.utn.frc.tup.lciii.dtos.common.RequestPost;
import ar.edu.utn.frc.tup.lciii.service.CountryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api")
public class CountryController {

    private final CountryService countryService;


    @GetMapping("countries")
    public ResponseEntity<List<CountryDTO>> getCountriesController(@RequestParam(required = false) String name){
        if(name!=null){
            return ResponseEntity.ok(countryService.getCountriesDTOByNameService(name));
        }else {
            return ResponseEntity.ok(countryService.getAllCountriesDTOService());
        }

    }
    @GetMapping("countries/{continent}/continent")
    public ResponseEntity<List<CountryDTO>> getContinent(@PathVariable String continent){
        return ResponseEntity.ok(countryService.getcontinentByContinent(continent));
    }
    @GetMapping("countries/{languaje}/languaje")
    public ResponseEntity<List<CountryDTO>> getContinentController(@PathVariable String continent){
        return ResponseEntity.ok(countryService.getcontinentBylenguage(continent));
    }


    @GetMapping("countries/most-borders")
    public ResponseEntity<List<CountryDTO>> getMostBordersController(){
        return ResponseEntity.ok(countryService.getCountryMoreBorders());
    }
    @PostMapping("countries")
    public ResponseEntity<List<CountryDTO>> createCountryController(@RequestBody RequestPost requestPost){
        return ResponseEntity.ok(countryService.postCountryAmount(requestPost));
    }


}