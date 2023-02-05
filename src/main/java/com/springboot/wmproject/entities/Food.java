package com.springboot.wmproject.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "food")
public class Food {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "food_name", length = 45)
    private String foodName;

    @Column(name = "food_type", length = 45)
    private String foodType;

    @Column(name = "description")
    private String description;

    @Column(name = "price", precision = 15, scale = 2)
    private Double price;


}