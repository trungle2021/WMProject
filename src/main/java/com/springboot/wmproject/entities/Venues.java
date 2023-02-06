package com.springboot.wmproject.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Venues {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "venue_name", nullable = true, length = 45)
    private String venueName;
    @Basic
    @Column(name = "min_people", nullable = true)
    private Integer minPeople;
    @Basic
    @Column(name = "max_people", nullable = true)
    private Integer maxPeople;
    @Basic
    @Column(name = "price", nullable = true, precision = 2)
    private Double price;
    @OneToMany(mappedBy = "venues",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<Orders> orders;



}
