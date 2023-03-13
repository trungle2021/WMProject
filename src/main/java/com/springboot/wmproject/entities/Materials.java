package com.springboot.wmproject.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;
@Getter
@Setter
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
    @Column(name = "food_id", nullable = true)
    private int foodId;
    @Basic
    @Column(name = "cost", nullable = true, precision = 2)
    private BigDecimal cost;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Food foodByFoodId;
}
