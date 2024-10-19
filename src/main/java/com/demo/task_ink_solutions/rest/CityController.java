package com.demo.task_ink_solutions.rest;

import com.demo.task_ink_solutions.model.City;
import com.demo.task_ink_solutions.repository.CityRepository;
import com.demo.task_ink_solutions.service.CityImportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
public class CityController {

    private final CityImportService cityImportService;
    private final CityRepository cityRepository;

    public CityController(CityImportService cityImportService, CityRepository cityRepository) {
        this.cityImportService = cityImportService;
        this.cityRepository = cityRepository;
    }

    @GetMapping(path = "cities")
    public ResponseEntity<List<City>> findAll(){
        return ResponseEntity.ok(cityRepository.findAll());
    }

    @PostMapping(path = "/import-cities")
    public ResponseEntity<String> importCities() {
        cityImportService.importCities();
        return ResponseEntity.ok("Cities imported successfully");
    }
}
