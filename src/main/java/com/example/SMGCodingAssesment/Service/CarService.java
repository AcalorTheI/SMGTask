package com.example.SMGCodingAssesment.Service;

import com.example.SMGCodingAssesment.elastic.model.Car;
import com.example.SMGCodingAssesment.elastic.repository.CarRepository;
import com.example.SMGCodingAssesment.kafka.events.CarEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Slf4j
@Service
@RequiredArgsConstructor
public class CarService {
    private final CarRepository carRepository;
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

        carRepository.save(newCar);
    }

    public void updateCar(CarEvent carEvent) {
        String id = carEvent.getCar().getId();
        Optional<Car> byId = carRepository.findById(id);
        if (byId.isEmpty()) {
            log.info("Message with key {} and timestmap {} was sent for update but car with id {} was not found in database.",
                    carEvent.getMsgKey(), carEvent.getTimestamp(), carEvent.getCar().getId());
            return;
        }

        Car updatedCar = carEvent.getCar();
        Car oldCar = byId.get();

        oldCar.setModel(updatedCar.getModel());
        oldCar.setYear(updatedCar.getYear());
        oldCar.setBrand(updatedCar.getBrand());
        oldCar.setPrice(updatedCar.getPrice());

        carRepository.save(oldCar);
    }

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

}
