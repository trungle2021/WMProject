package com.springboot.wmproject.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
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
    @Column(name = "material_code", nullable = true, length = 45,unique = true)
    private String materialCode;
    @Basic
    @Column(name = "material_name", nullable = true, length = 45)
    private String materialName;
    @Basic
    @Column(name = "unit", nullable = true, length = 45)
    private String unit;
    @Basic
    @Column(name = "price", nullable = true, precision = 2)
    private Double price;
    @OneToMany(mappedBy = "materialsByMaterialId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<MaterialDetail> materialsByMaterialId = new HashSet<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "food_id", referencedColumnName = "id",insertable = false,updatable = false)
//    private Food foodByFoodId;
}
