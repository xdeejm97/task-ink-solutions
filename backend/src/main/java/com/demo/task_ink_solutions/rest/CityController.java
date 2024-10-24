package com.demo.task_ink_solutions.rest;

import com.demo.task_ink_solutions.model.City;
import com.demo.task_ink_solutions.repository.CityRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api")
@CrossOrigin(origins = "http://localhost:3000")
public class CityController {

    private final CityRepository cityRepository;

    public CityController(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @GetMapping(path = "/cities")
    public ResponseEntity<List<City>> findCitiesByQuery(
            @RequestParam(required = false) String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "100") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<City> cities;

        if (query != null && !query.isEmpty()) {
            cities = cityRepository.findByNameContainingIgnoreCase(query, pageable);
        } else {
            cities = cityRepository.findAll(pageable);
        }
        return ResponseEntity.ok(cities.getContent());
    }
}
