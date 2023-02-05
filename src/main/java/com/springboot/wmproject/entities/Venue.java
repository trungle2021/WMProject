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
@Table(name = "venues")
public class Venue {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "venue_name", length = 45)
    private String venueName;

    @Column(name = "min_people")
    private Integer minPeople;

    @Column(name = "max_people")
    private Integer maxPeople;

    @Column(name = "price", precision = 15, scale = 2)
    private Double price;


}