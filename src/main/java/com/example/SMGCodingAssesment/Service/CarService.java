package com.example.SMGCodingAssesment.Service;

import com.example.SMGCodingAssesment.elastic.model.Car;
import com.example.SMGCodingAssesment.elastic.repository.CarRepository;
import com.example.SMGCodingAssesment.kafka.events.CarEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
    // Demonstration is done on id for caching
    @Cacheable(value = "cars", key = "#id")
    public Car getCarById(String id) {
        return carRepository.findById(id).orElse(null);
    }

    public void createCar(CarEvent carEvent) {
        Car newCar = Car.builder().
                brand(carEvent.getCar().getBrand()).
                year(carEvent.getCar().getYear()).
                model(carEvent.getCar().getModel()).
                price(carEvent.getCar().getPrice()).
                build();

        Car save = carRepository.save(newCar);
        log.info("Car product is saved in elastic with id {}", save.getId());
    }

    @CachePut(value = "cars", key = "#carEvent.car.id")
    public Car updateCar(CarEvent carEvent) {
        String id = carEvent.getCar().getId();
        Optional<Car> byId = carRepository.findById(id);
        if (byId.isEmpty()) {
            log.info("Message with key {} and timestamp {} was sent for update but car with id {} was not found in database.",
                    carEvent.getMsgKey(), carEvent.getTimestamp(), carEvent.getCar().getId());
            return null;
        }

        Car updatedCar = carEvent.getCar();
        Car oldCar = byId.get();

        oldCar.setModel(updatedCar.getModel());
        oldCar.setYear(updatedCar.getYear());
        oldCar.setBrand(updatedCar.getBrand());
        oldCar.setPrice(updatedCar.getPrice());

        return carRepository.save(oldCar);
    }

    @CacheEvict(value = "cars", key = "#carEvent.car.id")
    public void delete(CarEvent carEvent) {
        String id = carEvent.getCar().getId();
        Optional<Car> byId = carRepository.findById(id);
        if (byId.isEmpty()) {
            log.info("Message with key {} and timestmap {} was sent for deletion but car with id {} was not found in database.",
                    carEvent.getMsgKey(), carEvent.getTimestamp(), carEvent.getCar().getId());
            return;
        }
        carRepository.deleteById(id);
    }


    // This is the part how we would implement searching by any relevant field
    // We can also add aditional caches

    public Page<Car> getAllCars(Pageable pageable) {
        return carRepository.findAll(pageable);
    }

    // Foreexample we can create another cache here
    // @CachePut(value = "carsByBrand", key = "#brand")
    // But honestly i am not sure how to pair it with page
    public Page<Car> getAllByBrand(Pageable pageable, String brand) {
        return carRepository.findByBrandIgnoringCase(pageable, brand);
    }

    public Page<Car> getAllByYear(Pageable pageable, Integer year) {
        return carRepository.findByYear(pageable, year);
    }

    // Other option is this
    // For creating specification to filter everything and pageing together
    /*
    public static Specification<Car> hasMake(String make) {
        return (root, query, builder) ->
                builder.equal(builder.lower(root.get("make")), make.toLowerCase());
    }

    public static Specification<Car> hasYear(Integer year) {
        return (root, query, builder) ->
                builder.equal(root.get("year"), year);
    }

     */
}
