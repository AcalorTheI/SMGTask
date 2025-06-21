package com.example.SMGCodingAssesment.elastic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(indexName = "cars")
public class Car {
    // This will be UUID v4
    @Id
    private String id;
    private String brand;
    private String model;
    private Integer year;
    private Double price;
}
