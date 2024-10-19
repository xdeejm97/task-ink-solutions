package com.demo.task_ink_solutions.service;

import com.opencsv.CSVReader;
import com.demo.task_ink_solutions.model.City;
import com.demo.task_ink_solutions.repository.CityRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CityImportService {

    private final CityRepository cityRepository;

    private final RestTemplate restTemplate;

    public CityImportService(CityRepository cityRepository, RestTemplate restTemplate) {
        this.cityRepository = cityRepository;
        this.restTemplate = restTemplate;
    }

    public List<City> findAll(){
        return cityRepository.findAll();
    }

    public void importCities() {
        String url = "https://raw.githubusercontent.com/kelvins/US-Cities-Database/main/csv/us_cities.csv";
        String csvData = restTemplate.getForObject(url, String.class);

        try (var reader = new StringReader(csvData);
             var csvReader = new CSVReader(reader)) {

            csvReader.readNext();

            List<City> cities = new ArrayList<>();
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                City city = new City();
                city.setName(row[3]);
                city.setState(row[2]);
                cities.add(city);
            }

            cityRepository.saveAll(cities);
            System.out.println("Imported " + cities.size() + " cities.");
        } catch (Exception e) {
            throw new RuntimeException("Error importing cities", e);
        }
    }
}