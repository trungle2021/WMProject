package wm.clientmvc.entities;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import jakarta.persistence.*;

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
    @OneToMany(mappedBy = "foodByFoodId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<FoodDetails> foodDetailsById = new HashSet<>();
    @OneToMany(mappedBy = "foodByFoodId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<FoodImages> foodImagesById = new HashSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodType() {
        return foodType;
    }

    public void setFoodType(String foodType) {
        this.foodType = foodType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Food food = (Food) o;
        return id == food.id && Objects.equals(foodName, food.foodName) && Objects.equals(foodType, food.foodType) && Objects.equals(description, food.description) && Objects.equals(price, food.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, foodName, foodType, description, price);
    }

    public Collection<FoodDetails> getFoodDetailsById() {
        return foodDetailsById;
    }

    public void setFoodDetailsById(Collection<FoodDetails> foodDetailsById) {
        this.foodDetailsById = foodDetailsById;
    }

    public Collection<FoodImages> getFoodImagesById() {
        return foodImagesById;
    }

    public void setFoodImagesById(Collection<FoodImages> foodImagesById) {
        this.foodImagesById = foodImagesById;
    }
}
