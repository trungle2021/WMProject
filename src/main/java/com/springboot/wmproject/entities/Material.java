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
@Table(name = "materials")
public class Material {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "material_name", length = 45)
    private String materialName;

    @Column(name = "unit", length = 45)
    private String unit;

    @Column(name = "foodId", length = 45)
    private String foodId;

    @Column(name = "cost", precision = 15, scale = 2)
    private Double cost;



}