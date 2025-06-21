package com.example.SMGCodingAssesment.kafka.events;

import com.example.SMGCodingAssesment.elastic.model.Car;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarEvent {
    private int timestamp;
    private String msgKey;
    private EventType eventType;
    private Car car;
}
