package com.example.SMGCodingAssesment.integration;

import com.example.SMGCodingAssesment.elastic.model.Car;
import com.example.SMGCodingAssesment.elastic.repository.CarRepository;
import com.example.SMGCodingAssesment.kafka.events.CarEvent;
import com.example.SMGCodingAssesment.kafka.events.EventType;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.elasticsearch.ElasticsearchContainer;
import org.testcontainers.shaded.com.fasterxml.jackson.core.type.TypeReference;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.JsonNode;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;
import org.testcontainers.shaded.org.awaitility.Awaitility;
import org.testcontainers.utility.DockerImageName;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
@AutoConfigureMockMvc
public class CarControllerByIdIntegrationTest {
    // Ideally we would have abstract Base Integration class and put these there
    // and all integration classes would extend it
    static final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.5.0"));
    static final ElasticsearchContainer elasticsearch =
            new ElasticsearchContainer(DockerImageName.parse("docker.elastic.co/elasticsearch/elasticsearch:8.12.1"))
                    .withEnv("xpack.security.enabled", "false");

    static {
        kafka.start();
        elasticsearch.start();
    }

    @DynamicPropertySource
    static void registerProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.kafka.bootstrap-servers", kafka::getBootstrapServers);
        registry.add("spring.elasticsearch.uris", elasticsearch::getHttpHostAddress);
    }


    // One integration test for creating new car just to demonstrate how
    // Test Containers run and work in spring test

    @Autowired
    public Gson gson;

    @Value("${topic.name}")
    private String topic;

    @Autowired
    public KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CarRepository carRepository;

    @BeforeEach
    void beforeEach() {
        carRepository.deleteAll();;
    }

    @Test
    public void testCreationOfObjectFromKafkaMessage() throws Exception {
        // Given kafka message is sent
        createAndSendCarMessage();

        // Trick to wait for kafka message to be sent and read and to check for
        // value of the id in database
        AtomicReference<Car> carAtomicReference = new AtomicReference<>();
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    assertTrue(carRepository.findAll().iterator().hasNext());
                    carAtomicReference.set(carRepository.findAll().iterator().next()); // Dirty trick to get id
                });
        Car next = carAtomicReference.get(); // Dirty trick to get id
        // When we fire get request by id
        // Then
        String contentAsString = mockMvc.perform(get("/cars/search/" + next.getId())).
                andReturn().
                getResponse().
                getContentAsString();

        // Then we get the object from kafka message
        Car car = gson.fromJson(contentAsString, Car.class);
        assertEquals(2025, car.getYear());
        assertEquals(next.getId(), car.getId());
        assertTrue(next.getBrand().contains("Acalor"));
    }

    // Standard way to test a controller

    @Test
    public void testGettingObjectById() throws Exception {
        // Given object exists
        Car car = Car.builder().
                price(10_000.0).
                brand("Acalor").
                year(2025).
                model("Model" + UUID.randomUUID()).build();
        Car save = carRepository.save(car);


        // When we fire get request by id
        String contentAsString = mockMvc.perform(get("/cars/search/" + save.getId())).
                andReturn().
                getResponse().
                getContentAsString();

        // Then // compare whats really inside databse with what we got from rest
        car = gson.fromJson(contentAsString, Car.class);
        assertEquals(2025, car.getYear());
        assertEquals(save.getId(), car.getId());
        assertTrue(save.getBrand().contains("Acalor"));

    }

    // Testing with pageable
    @Test
    public void testGettingAllWithPageable() throws Exception {
        // Given lots of objects exist
        for (int i = 0; i < 100; i++) {
            Car car = Car.builder().
                    price(10_000.0).
                    brand("Acalor").
                    year(i).
                    model(UUID.randomUUID().toString()).build();
            carRepository.save(car);
        }


        // When we fire get request by id
        String contentAsString = mockMvc.perform(get("/cars/search").
                param("page", "0").
                param("size", "10").
                param("sort", "year,asc")
                ).
                andReturn().
                getResponse().
                getContentAsString();

        // Then
        // compare whats really inside databse with what we got from rest
        // We can for example check if it sorted by id or timestamp or whatever

        // Extract the 'content' JSON array
        JsonObject root = JsonParser.parseString(contentAsString).getAsJsonObject();
        String contentJson = root.getAsJsonArray("content").toString();

        List<Car> page = gson.fromJson(contentJson, new TypeReference<List<Car>>() {}.getType());

        for (int i = 0; i < 10; i++){
            // Chast checking if it is sorted by model ascending
            assertEquals(i, page.get(i).getYear());
        }

    }


    private void createAndSendCarMessage() {
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

}
