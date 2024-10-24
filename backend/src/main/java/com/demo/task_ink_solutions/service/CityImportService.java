package com.demo.task_ink_solutions.service;

import com.demo.task_ink_solutions.model.City;
import com.demo.task_ink_solutions.repository.CityRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;


@Service
public class CityImportService {

    private static final Logger logger = LoggerFactory.getLogger(CityImportService.class);

    private final CityRepository cityRepository;

    private final RestTemplate restTemplate;

    public CityImportService(CityRepository cityRepository, RestTemplate restTemplate) {
        this.cityRepository = cityRepository;
        this.restTemplate = restTemplate;
    }

    @PostConstruct
    public void init() {
        if(cityRepository.count() == 0) {
            importCities();
        }
    }

    public List<City> findAll(){
        return cityRepository.findAll();
    }

    public void importCities() {
        String url = "https://raw.githubusercontent.com/kelvins/US-Cities-Database/main/csv/us_cities.csv";
        String csvData = restTemplate.getForObject(url, String.class);

        if (csvData == null) {
            throw new RuntimeException("Failed to retrieve CSV data from the URL.");
        }

        try (var reader = new StringReader(csvData);
             var csvReader = new CSVReader(reader)) {

            csvReader.readNext();

            int counter = 0;
            String[] row;
            while ((row = csvReader.readNext()) != null) {
                City city = new City();
                city.setName(row[3]);
                city.setState(row[2]);
                counter++;
                cityRepository.save(city);
            }

            logger.info("Imported {} cities.", counter);
        } catch (IOException | CsvException e) {
            logger.error("Error importing cities", e);
            throw new RuntimeException("Error importing cities", e);
        }
    }
}