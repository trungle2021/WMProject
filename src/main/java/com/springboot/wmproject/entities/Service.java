package com.springboot.wmproject.entities;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "services")
public class Service {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "service_name", length = 45)
    private String serviceName;

    @Column(name = "price", precision = 15, scale = 2)
    private Double price;

    @Column(name = "description")
    private String description;



}