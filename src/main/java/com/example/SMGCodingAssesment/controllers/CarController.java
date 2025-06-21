package com.example.SMGCodingAssesment.controllers;

import com.example.SMGCodingAssesment.Service.CarService;
import com.example.SMGCodingAssesment.elastic.model.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


// This is just to see something in postman
@RequestMapping("/car")
@RestController
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable(name = "id") String id) {
        Car carById = carService.getCarById(id);
        return carById != null ? ResponseEntity.ok(carById) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/")
    public ResponseEntity<List<Car>> getAllCars(@PathVariable(name = "id") String id) {
        Car carById = carService.getCarById(id);
        return carById != null ? ResponseEntity.ok(List.of(carById)) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }
}
