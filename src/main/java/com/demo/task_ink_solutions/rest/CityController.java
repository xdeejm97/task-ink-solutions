package com.demo.task_ink_solutions.rest;

import com.demo.task_ink_solutions.model.City;
import com.demo.task_ink_solutions.repository.CityRepository;
import com.demo.task_ink_solutions.service.CityImportService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CityController {

    private final CityImportService cityImportService;
    private final CityRepository cityRepository;

    public CityController(CityImportService cityImportService, CityRepository cityRepository) {
        this.cityImportService = cityImportService;
        this.cityRepository = cityRepository;
    }

//    @GetMapping(path = "/cities")
//    public ResponseEntity<List<City>> findAll(){
//        return ResponseEntity.ok(cityRepository.findAll());
//    }

    @GetMapping(path = "/cities")
    public ResponseEntity<List<City>> findCitiesByQuery(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<City> cities;

        // Querying cities based on input
        if (query != null && !query.isEmpty()) {
            cities = cityRepository.findByNameContainingIgnoreCase(query, pageable);
        } else {
            cities = cityRepository.findAll(pageable);
        }

        // Return only the content of the page (list of cities)
        return ResponseEntity.ok(cities.getContent());
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
