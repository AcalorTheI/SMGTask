package com.example.SMGCodingAssesment.elastic.repository;

import com.example.SMGCodingAssesment.elastic.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends ElasticsearchRepository<Car, String> {
    // We can make search by any field inside spring data and make appropriate rest endpoints
    // like this but i only care for the id since every idea can be demonstrated with it.
    Page<Car> findByYear(Pageable pageable, Integer year);

    Page<Car> findByBrandIgnoringCase(Pageable pageable, String brand);

    // we can ofc add anything we want here make price or whatever
}