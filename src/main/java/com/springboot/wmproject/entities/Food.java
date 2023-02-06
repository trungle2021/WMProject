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