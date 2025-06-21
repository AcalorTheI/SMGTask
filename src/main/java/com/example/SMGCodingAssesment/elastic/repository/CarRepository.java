package com.example.SMGCodingAssesment.elastic.repository;

import com.example.SMGCodingAssesment.elastic.model.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends ElasticsearchRepository<Car, String> {
    List<Car> findByModelContainingIgnoreCase(String model);
    List<Car> findByYear(Integer year);
}