package wm.clientmvc.entities;

import java.util.Objects;
import jakarta.persistence.*;

@Entity
@Table(name = "service_details", schema = "wmproject", catalog = "")
public class ServiceDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "order_id", nullable = true)
    private Integer orderId;
    @Basic
    @Column(name = "service_id", nullable = true)
    private Integer serviceId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Orders ordersByOrderId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "service_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Services servicesByServiceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getServiceId() {
        return serviceId;
    }

    public void setServiceId(Integer serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ServiceDetails that = (ServiceDetails) o;
        return id == that.id && Objects.equals(orderId, that.orderId) && Objects.equals(serviceId, that.serviceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderId, serviceId);
    }

    public Orders getOrdersByOrderId() {
        return ordersByOrderId;
    }

    public void setOrdersByOrderId(Orders ordersByOrderId) {
        this.ordersByOrderId = ordersByOrderId;
    }

    public Services getServicesByServiceId() {
        return servicesByServiceId;
    }

    public void setServicesByServiceId(Services servicesByServiceId) {
        this.servicesByServiceId = servicesByServiceId;
    }
}
