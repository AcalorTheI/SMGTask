package com.example.SMGCodingAssesment.elastic.repository;

import com.example.SMGCodingAssesment.elastic.model.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarRepository extends ElasticsearchRepository<Car, String> {
    // We can make search by any field inside spring data and make appropriate rest endpoints
    // like this but i only care for the id since every idea can be demonstrated with it.
    List<Car> findByModelContainingIgnoreCase(String model);
    List<Car> findByYear(Integer year);
}