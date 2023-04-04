package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "material_detail", uniqueConstraints = @UniqueConstraint(columnNames={"food_id", "material_id"}))
@Entity
public class MaterialDetail {


    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "material_id", nullable = true)
    private Integer materialId;
    @Basic
    @Column(name = "food_id", nullable = true)
    private Integer foodId;

    @Basic
    @Column(name = "count", nullable = true, precision = 2)
    private Double count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "material_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Materials materialsByMaterialId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Food foodByFoodId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getMaterialId() {
        return materialId;
    }

    public void setMaterialId(Integer materialId) {
        this.materialId = materialId;
    }

    public Integer getFoodId() {
        return foodId;
    }

    public void setFoodId(Integer foodId) {
        this.foodId = foodId;
    }


    public Double getCount() {
        return count;
    }

    public void setCount(Double count) {
        this.count = count;
    }

    public Materials getMaterialsByMaterialId() {
        return materialsByMaterialId;
    }

    public void setMaterialsByMaterialId(Materials materialsByMaterialId) {
        this.materialsByMaterialId = materialsByMaterialId;
    }

    public Food getFoodByFoodId() {
        return foodByFoodId;
    }

    public void setFoodByFoodId(Food foodByFoodId) {
        this.foodByFoodId = foodByFoodId;
    }
}
