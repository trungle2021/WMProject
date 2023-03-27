package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.Type;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

@Entity
public class Venues {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @NotEmpty
    @Basic
    @Column(name = "venue_name", nullable = true, length = 45)
    private String venueName;
    @Max(3000)
    @Min(1)
    @Basic
    @Column(name = "min_people", nullable = true)
    private Integer minPeople;
    @Max(3000)
    @Min(1)
    @Basic
    @Column(name = "max_people", nullable = true)
    private Integer maxPeople;
    @Min(1)
    @Basic
    @Column(name = "price", nullable = true, precision = 2)
    private Double price;
    @Basic
    @Column(nullable = false, columnDefinition = "TINYINT(1)", length = 1)
    private boolean active;
    @OneToMany(mappedBy = "venues", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Orders> ordersById=new HashSet<>();
    @OneToMany(mappedBy = "venues", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<VenueImages> venueImagesById=new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public Integer getMinPeople() {
        return minPeople;
    }

    public void setMinPeople(Integer minPeople) {
        this.minPeople = minPeople;
    }

    public Integer getMaxPeople() {
        return maxPeople;
    }

    public void setMaxPeople(Integer maxPeople) {
        this.maxPeople = maxPeople;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venues venues = (Venues) o;
        return id == venues.id && Objects.equals(venueName, venues.venueName) && Objects.equals(minPeople, venues.minPeople) && Objects.equals(maxPeople, venues.maxPeople) && Objects.equals(price, venues.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, venueName, minPeople, maxPeople, price);
    }

    public Collection<Orders> getOrdersById() {
        return ordersById;
    }

    public void setOrdersById(Collection<Orders> ordersById) {
        this.ordersById = ordersById;
    }

    public Collection<VenueImages> getVenueImagesById() {
        return venueImagesById;
    }

    public void setVenueImagesById(Collection<VenueImages> venueImagesById) {
        this.venueImagesById = venueImagesById;
    }
}
