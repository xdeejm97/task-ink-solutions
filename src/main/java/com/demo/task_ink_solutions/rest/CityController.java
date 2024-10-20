package com.demo.task_ink_solutions.rest;

import com.demo.task_ink_solutions.model.City;
import com.demo.task_ink_solutions.repository.CityRepository;
import com.demo.task_ink_solutions.service.CityImportService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/api")
public class CityController {

    private final CityImportService cityImportService;
    private final CityRepository cityRepository;

    public CityController(CityImportService cityImportService, CityRepository cityRepository) {
        this.cityImportService = cityImportService;
        this.cityRepository = cityRepository;
    }

    @GetMapping(path = "/cities")
    public ResponseEntity<List<City>> findAll(){
        return ResponseEntity.ok(cityRepository.findAll());
    }

    @PostMapping(path = "/import-cities")
    public ResponseEntity<?> importCities() {
        try {
            CompletableFuture.runAsync(cityImportService::importCities);
            return ResponseEntity.accepted().body("City import process started");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to start city import: " + e.getMessage());
        }
    }
}
