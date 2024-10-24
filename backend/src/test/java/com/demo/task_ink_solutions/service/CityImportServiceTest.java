package com.demo.task_ink_solutions.service;

import com.demo.task_ink_solutions.model.City;
import com.demo.task_ink_solutions.repository.CityRepository;
import com.opencsv.exceptions.CsvException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CityImportServiceTest {


    @Mock
    private CityRepository cityRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private CityImportService cityImportService;

    private static final String MOCK_CSV_DATA =
            "id,state_name,city_name\n" +
                    "1,California,Los Angeles" +
                    "2,New York,New York";

    @Test
    void testFindAll_ReturnsCities() {
        City city1 = new City();
        city1.setName("Los Angeles");
        City city2 = new City();
        city2.setName("New York");

        when(cityRepository.findAll()).thenReturn(Arrays.asList(city1, city2));

        List<City> cities = cityImportService.findAll();

        assertEquals(2, cities.size());
        assertEquals("Los Angeles", cities.get(0).getName());
        assertEquals("New York", cities.get(1).getName());
    }

    @Test
    void testFindAll_ReturnsEmptyList() {
        when(cityRepository.findAll()).thenReturn(Collections.emptyList());

        List<City> cities = cityImportService.findAll();

        assertEquals(0, cities.size());
    }


    @Test
    void testImportCities_RestTemplateReturnsNull() {
        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(null);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> cityImportService.importCities());
        assertEquals("Failed to retrieve CSV data from the URL.", exception.getMessage());
    }
}