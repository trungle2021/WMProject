package wm.clientmvc.entities;

import jakarta.persistence.*;
import java.util.Collection;
import java.util.Objects;

@Entity
public class Orders {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id", nullable = false)
    private int id;
    @Basic
    @Column(name = "order_date", nullable = true, length = 45)
    private String orderDate;
    @Basic
    @Column(name = "order_status", nullable = true, length = 45)
    private String orderStatus;
    @Basic
    @Column(name = "order_total", nullable = true, precision = 2)
    private Double orderTotal;
    @Basic
    @Column(name = "time_happen", nullable = true, length = 20)
    private String timeHappen;
    @Basic
    @Column(name = "venue_id", nullable = true)
    private Integer venueId;
    @Basic
    @Column(name = "booking_emp", nullable = true)
    private Integer bookingEmp;
    @Basic
    @Column(name = "organize_team", nullable = true)
    private Integer organizeTeam;
    @Basic
    @Column(name = "customer_id", nullable = true)
    private Integer customerId;
    @Basic
    @Column(name = "table_amount", nullable = true)
    private Integer tableAmount;
    @Basic
    @Column(name = "part_time_emp_amount", nullable = true)
    private Integer partTimeEmpAmount;
    @OneToMany(mappedBy = "ordersByOrderId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<FoodDetails> foodDetailsById;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Venues venuesByVenueId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_emp", referencedColumnName = "id",insertable = false,updatable = false)
    private Employees employeesByBookingEmp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organize_team", referencedColumnName = "id",insertable = false,updatable = false)
    private OrganizeTeams organizeTeamsByOrganizeTeam;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id",insertable = false,updatable = false)
    private Customers customersByCustomerId;
    @OneToMany(mappedBy = "ordersByOrderId",cascade = CascadeType.ALL,orphanRemoval = true)
    private Collection<ServiceDetails> serviceDetailsById;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Double getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(Double orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getTimeHappen() {
        return timeHappen;
    }

    public void setTimeHappen(String timeHappen) {
        this.timeHappen = timeHappen;
    }

    public Integer getVenueId() {
        return venueId;
    }

    public void setVenueId(Integer venueId) {
        this.venueId = venueId;
    }

    public Integer getBookingEmp() {
        return bookingEmp;
    }

    public void setBookingEmp(Integer bookingEmp) {
        this.bookingEmp = bookingEmp;
    }

    public Integer getOrganizeTeam() {
        return organizeTeam;
    }

    public void setOrganizeTeam(Integer organizeTeam) {
        this.organizeTeam = organizeTeam;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public Integer getTableAmount() {
        return tableAmount;
    }

    public void setTableAmount(Integer tableAmount) {
        this.tableAmount = tableAmount;
    }

    public Integer getPartTimeEmpAmount() {
        return partTimeEmpAmount;
    }

    public void setPartTimeEmpAmount(Integer partTimeEmpAmount) {
        this.partTimeEmpAmount = partTimeEmpAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Orders orders = (Orders) o;
        return id == orders.id && Objects.equals(orderDate, orders.orderDate) && Objects.equals(orderStatus, orders.orderStatus) && Objects.equals(orderTotal, orders.orderTotal) && Objects.equals(timeHappen, orders.timeHappen) && Objects.equals(venueId, orders.venueId) && Objects.equals(bookingEmp, orders.bookingEmp) && Objects.equals(organizeTeam, orders.organizeTeam) && Objects.equals(customerId, orders.customerId) && Objects.equals(tableAmount, orders.tableAmount) && Objects.equals(partTimeEmpAmount, orders.partTimeEmpAmount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderDate, orderStatus, orderTotal, timeHappen, venueId, bookingEmp, organizeTeam, customerId, tableAmount, partTimeEmpAmount);
    }

    public Collection<FoodDetails> getFoodDetailsById() {
        return foodDetailsById;
    }

    public void setFoodDetailsById(Collection<FoodDetails> foodDetailsById) {
        this.foodDetailsById = foodDetailsById;
    }

    public Venues getVenuesByVenueId() {
        return venuesByVenueId;
    }

    public void setVenuesByVenueId(Venues venuesByVenueId) {
        this.venuesByVenueId = venuesByVenueId;
    }

    public Employees getEmployeesByBookingEmp() {
        return employeesByBookingEmp;
    }

    public void setEmployeesByBookingEmp(Employees employeesByBookingEmp) {
        this.employeesByBookingEmp = employeesByBookingEmp;
    }

    public OrganizeTeams getOrganizeTeamsByOrganizeTeam() {
        return organizeTeamsByOrganizeTeam;
    }

    public void setOrganizeTeamsByOrganizeTeam(OrganizeTeams organizeTeamsByOrganizeTeam) {
        this.organizeTeamsByOrganizeTeam = organizeTeamsByOrganizeTeam;
    }

    public Customers getCustomersByCustomerId() {
        return customersByCustomerId;
    }

    public void setCustomersByCustomerId(Customers customersByCustomerId) {
        this.customersByCustomerId = customersByCustomerId;
    }

    public Collection<ServiceDetails> getServiceDetailsById() {
        return serviceDetailsById;
    }

    public void setServiceDetailsById(Collection<ServiceDetails> serviceDetailsById) {
        this.serviceDetailsById = serviceDetailsById;
    }
}
