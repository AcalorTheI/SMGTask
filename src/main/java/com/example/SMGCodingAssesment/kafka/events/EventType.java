package com.example.SMGCodingAssesment.kafka.events;

public enum EventType {
    CAR_CREATED("car_created"),
    CAR_UPDATE("car_updated"),
    CAR_DELETED("car_deleted");

    private final String name;

    EventType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
