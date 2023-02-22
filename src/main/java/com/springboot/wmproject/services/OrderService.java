package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.OrderDTO;

import java.util.List;

public interface OrderService {
    List<OrderDTO> getAllOrder();
    List<OrderDTO> getAllByOrderDate(String orderDate);
    List<OrderDTO> getAllByTimeHappen(String time);
    List<OrderDTO> getAllByVenueId(int id);
    List<OrderDTO> getAllByBookingEmp(int id);
    List<OrderDTO> getAllByOrganizeTeam(int id);
    List<OrderDTO> getAllByCustomerId(int id);
    List<OrderDTO> getAllByOrderStatus(String status);
    OrderDTO getOneOrderByOrderId(int id);
    OrderDTO createOrder(OrderDTO orderDTO);
    OrderDTO updateOrder(OrderDTO orderDTO);
    void deleteOrder(int id);
    public OrderDTO updateOrderStatus(Integer orderDTOId,String status);
}
