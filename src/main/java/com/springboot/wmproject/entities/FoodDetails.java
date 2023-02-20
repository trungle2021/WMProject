package com.springboot.wmproject.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
@Table(name = "food_details", schema = "wmproject", catalog = "")
public class FoodDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "order_id", nullable = true)
    private Integer orderId;
    @Basic
    @Column(name = "food_id", nullable = true)
    private Integer foodId;
    @Basic
    @Column(name = "count", nullable = true)
    private Integer count;
    @Basic
    @Column(name = "price", nullable = true, precision = 2)
    private Double price;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private Orders ordersByOrderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", referencedColumnName = "id",nullable = false,insertable = false,updatable = false)
    private Food foodByFoodId;


}
