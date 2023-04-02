package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Food {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "food_name", nullable = true, length = 45)
    private String foodName;
    @Basic
    @Column(name = "food_type", nullable = true, length = 45)
    private String foodType;
    @Basic
    @Column(name = "description", nullable = true, length = 255)
    private String description;
    @Basic
    @Column(name = "price", nullable = true, precision = 2)
    private Double price;
    @Basic
    @Column(nullable = false, columnDefinition = "TINYINT(1)", length = 1)
    private boolean active;
    @OneToMany(mappedBy = "foodByFoodId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<FoodDetails> foodDetailsById = new HashSet<>();
    @OneToMany(mappedBy = "foodByFoodId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<FoodImages> foodImagesById = new HashSet<>();
    @OneToMany(mappedBy = "foodByFoodId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<MaterialDetail> materialDetailById = new HashSet<>();

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
