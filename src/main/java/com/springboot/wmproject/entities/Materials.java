package com.springboot.wmproject.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Materials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "material_name", nullable = true, length = 45)
    private String materialName;
    @Basic
    @Column(name = "unit", nullable = true, length = 45)
    private String unit;
    @Basic
    @Column(name = "food_id", nullable = true, length = 45)
    private String foodId;
    @Basic
    @Column(name = "cost", nullable = true, precision = 2)
    private Double cost;
}
