package com.demo.task_ink_solutions.repository;

import com.demo.task_ink_solutions.model.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findByNameIgnoreCase(String name);
}
