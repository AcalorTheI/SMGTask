package com.example.SMGCodingAssesment.elastic.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

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
    @Field(type = FieldType.Text, analyzer = "standard")
    private String brand;
    @Field(type = FieldType.Text, analyzer = "standard")
    private String model;
    @Field(type = FieldType.Integer)
    private Integer year;
    @Field(type = FieldType.Double)
    private Double price;
}
