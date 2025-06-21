package com.example.SMGCodingAssesment.MessageProducer;

import com.example.SMGCodingAssesment.elastic.model.Car;
import com.example.SMGCodingAssesment.kafka.events.CarEvent;
import com.example.SMGCodingAssesment.kafka.events.EventType;
import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Instant;
import java.util.UUID;

@SpringBootTest
public class SendMessages {
    @Autowired
    public Gson gson;

    @Value("${topic.name}")
    private String topic;

    @Autowired
    public KafkaTemplate<String, String> kafkaTemplate;

    @Test
    public void  sendCreateKafkaMessage() {
        Car car = Car.builder().
                price(10_000.0).
                brand("Acalor").
                year(2025).
                model("Model" + UUID.randomUUID()).build();

        CarEvent carEvent = CarEvent.builder().
                car(car).
                msgKey(UUID.randomUUID().toString()).
                timestamp(Instant.now().getNano()).
                eventType(EventType.CAR_CREATED).
                build();

        String jsonMessage = gson.toJson(carEvent);
        kafkaTemplate.send(topic, carEvent.getMsgKey(), jsonMessage);

    }


    // This updates the price
    @Test
    public void sendUpdateKafkaMessage() {
        Car car = Car.builder().
                id("XSFNkpcBtgkrRuDgmYrr"). // When you save the product through the first test copy paste from the logs the id
                //  It will look something like this Car product is saved in elastic with id NyEWkpcBtgkrRuDgZYpz
                price(10_000.0*23124). // lets pretend that this is a buggati from the future ;D
                brand("Acalor").
                year(2100).
                model("Model" + UUID.randomUUID()).
                build();

        CarEvent carEvent = CarEvent.builder().
                car(car).
                msgKey(UUID.randomUUID().toString()).
                timestamp(Instant.now().getNano()).
                eventType(EventType.CAR_UPDATE).
                build();

        String jsonMessage = gson.toJson(carEvent);
        kafkaTemplate.send(topic, carEvent.getMsgKey(), jsonMessage);

    }

    // This Deletes from elastic cache
    @Test
    public void sendDeleteKafkaMessage() {
        Car car = Car.builder().
                id("XSFNkpcBtgkrRuDgmYrr"). // Basically we care only for id here
                build();

        CarEvent carEvent = CarEvent.builder().
                car(car).
                msgKey(UUID.randomUUID().toString()).
                timestamp(Instant.now().getNano()).
                eventType(EventType.CAR_DELETED).
                build();

        String jsonMessage = gson.toJson(carEvent);
        kafkaTemplate.send(topic, carEvent.getMsgKey(), jsonMessage);

    }
}
