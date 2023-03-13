package wm.clientmvc.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

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
    @OneToMany(mappedBy = "foodByFoodId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<FoodDetails> foodDetailsById = new HashSet<>();
    @OneToMany(mappedBy = "foodByFoodId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<FoodImages> foodImagesById = new HashSet<>();
    @OneToMany(mappedBy = "foodByFoodId", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<Materials> materialsById = new HashSet<>();

}
