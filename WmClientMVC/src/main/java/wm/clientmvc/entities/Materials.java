package wm.clientmvc.entities;
import java.math.BigDecimal;
import java.util.Objects;
import jakarta.persistence.*;

@Entity
public class Materials {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "material_name", nullable = true, length = 45)
    private String materialName;
    @Basic
    @Column(name = "unit", nullable = true, length = 45)
    private String unit;
    @Basic
    @Column(name = "food_id", nullable = true, length = 45)
    private String foodId;
    @Basic
    @Column(name = "cost", nullable = true, precision = 2)
    private BigDecimal cost;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Materials materials = (Materials) o;
        return id == materials.id && Objects.equals(materialName, materials.materialName) && Objects.equals(unit, materials.unit) && Objects.equals(foodId, materials.foodId) && Objects.equals(cost, materials.cost);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, materialName, unit, foodId, cost);
    }
}
