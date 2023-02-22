package com.springboot.wmproject.entities;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Services {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "service_name", nullable = true, length = 45)
    private String serviceName;
    @Basic
    @Column(name = "price", nullable = true, precision = 2)
    private BigDecimal price;
    @Basic
    @Column(name = "description", nullable = true, length = 255)
    private String description;
    @OneToMany(mappedBy = "servicesByServiceId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<ServiceDetails> serviceDetailsById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Services services = (Services) o;
        return id == services.id && Objects.equals(serviceName, services.serviceName) && Objects.equals(price, services.price) && Objects.equals(description, services.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, serviceName, price, description);
    }

    public Collection<ServiceDetails> getServiceDetailsById() {
        return serviceDetailsById;
    }

    public void setServiceDetailsById(Collection<ServiceDetails> serviceDetailsById) {
        this.serviceDetailsById = serviceDetailsById;
    }
}
