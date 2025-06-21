package com.example.SMGCodingAssesment.kafka;

import com.example.SMGCodingAssesment.Service.CarService;
import com.example.SMGCodingAssesment.kafka.events.CarEvent;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class KafkaService {
    private final Gson gson;
    private final CarService carService;

    @KafkaListener(topics = "${topic.name}", groupId = "car-event-consumer-group")
    public void listen(String event) {
        if (event.contains("fail")) {
            throw new RuntimeException("Some error");
        }

        CarEvent carEvent = gson.fromJson(event, CarEvent.class);
        log.info("Received car event: {} | Car: {} with timestamp: {} and type: {}", carEvent.getEventType(),
                carEvent.getCar().getBrand() + " " + carEvent.getCar().getModel(), carEvent.getTimestamp(),
                carEvent.getEventType());

        switch (carEvent.getEventType()) {
            case CAR_CREATED -> carService.createCar(carEvent);
            case CAR_UPDATE -> carService.updateCar(carEvent);
            case CAR_DELETED -> carService.delete(carEvent);
            default -> throw new RuntimeException("Unrecognized event type: " + carEvent.getEventType());
        }

    }
    @KafkaListener(topics = "${topic.name}.DLQ", groupId = "dlt-group")
    public void handleDlt(String message) {
        System.out.println("Handling message in DLQ: " + message);
        // We can do something here either log and save the data from the message or send them
        // again to the topic
    }


}
