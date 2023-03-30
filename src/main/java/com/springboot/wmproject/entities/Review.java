package com.springboot.wmproject.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
public class Review {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "content", nullable = false)
    private String content;
    @Basic
    @Column(name = "date_post", nullable = false)
    private String datePost;
    @Basic
    @Column(name = "rating", nullable = false)
    private double rating;
    @Basic
    @Column(name = "active", nullable = false)
    private boolean active;
    @Basic
    @Column(name = "customerAccountId", nullable = false)
    private Integer customerAccountId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customerAccountId", referencedColumnName = "id",insertable = false,updatable = false)
    private CustomerAccounts reviewByCustomerAccountId;

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
