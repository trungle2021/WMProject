package com.springboot.wmproject.repositories;

import com.springboot.wmproject.entities.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Orders,Integer> {
    @Query("select o from Orders o where o.customerId=:customerId")
    List<Orders> findByCustomerId(int customerId);
    @Query("select o from Orders o where o.orderDate=:orderDate")
    List<Orders> findByOrderDate(String orderDate);
    @Query("select o from Orders o where o.timeHappen=:time")
    List<Orders> findByTimeHappen(String time);
    @Query("select o from Orders o where o.venueId=:id")
    List<Orders> findByVenueId(int id);
    @Query("select o from Orders o where o.bookingEmp=:id")
    List<Orders> findByBookingEmp(int id);
    @Query("select o from Orders o where o.organizeTeam=:id")
    List<Orders> findByOrganizeTeamId(int id);
    @Query("select o from Orders o where o.orderStatus=:status")
    List<Orders> findByOrderStatus(String status);
    @Query("select o from Orders o where o.timeHappen=:time and o.venueId=:venueId") // COI LẠI SAU BUSINESS LOGIC VỀ KHUNG GIO
    List<Orders> orderListInTimeVenue(String time,int venueId);

    @Query("SELECT NEW Orders(o.id, o.orderStatus, o.timeHappen, o.venueId) FROM Orders o")
    List<Orders> findAllToGetVenue();
}
