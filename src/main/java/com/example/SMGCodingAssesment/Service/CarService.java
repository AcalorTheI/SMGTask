package com.example.SMGCodingAssesment.Service;

import com.example.SMGCodingAssesment.elastic.model.Car;
import com.example.SMGCodingAssesment.elastic.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    public Car getCarById(String id) {
        return carRepository.findById(id).orElse(null);
    }
}
