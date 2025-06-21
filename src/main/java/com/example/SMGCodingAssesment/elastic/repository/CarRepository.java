package com.example.SMGCodingAssesment.elastic.repository;

import com.example.SMGCodingAssesment.elastic.model.Car;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends ElasticsearchRepository<Car, String> {
}