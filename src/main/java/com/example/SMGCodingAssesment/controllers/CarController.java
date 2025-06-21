package com.example.SMGCodingAssesment.controllers;

import com.example.SMGCodingAssesment.Service.CarService;
import com.example.SMGCodingAssesment.elastic.model.Car;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


// This is just to see something in postman
@RequestMapping("/cars")
@RestController
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    @GetMapping("/search/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable(name = "id") String id) {
        Car carById = carService.getCarById(id);
        return carById != null ? ResponseEntity.ok(carById) :
                ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    @GetMapping("/search")
    public Page<Car> getCars(Pageable pageable) {
        return carService.getAllCars(pageable);
    }


    // Searching all by year
    @GetMapping("/search/year/{year}")
    public Page<Car> getAllByYear(Pageable pageable, @PathVariable("year") Integer year) {
        return carService.getAllByYear(pageable, year);
    }

    @GetMapping("/search/brand/{brand}")
    public Page<Car> getAllByBrand(Pageable pageable, @PathVariable("brand") String brand) {
        return carService.getAllByBrand(pageable, brand);
    }



}
