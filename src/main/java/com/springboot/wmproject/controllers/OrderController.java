package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.OrderDTO;

import com.springboot.wmproject.services.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }
@GetMapping
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<List<OrderDTO>> getAllOrder()
    {
        return  ResponseEntity.ok(orderService.getAllOrder());

    }
    @GetMapping("/bybookingEmp")
    public ResponseEntity<List<OrderDTO>> getAllOrderbyBooking(Integer empId)
    {
        return  ResponseEntity.ok(orderService.getAllByBookingEmp(empId));

    }
    @GetMapping("/byTeam")
    public ResponseEntity<List<OrderDTO>> getAllOrderbyTeam(Integer teamId)
    {
        return  ResponseEntity.ok(orderService.getAllByOrganizeTeam(teamId));

    }

    @PostMapping("create")
    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderDTO> create(@RequestBody OrderDTO order)
    {
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }
    @PutMapping("/update")
    public ResponseEntity<OrderDTO> update(@RequestBody OrderDTO order)
    {
        return ResponseEntity.ok(orderService.updateOrder(order));


    }


    @PutMapping("/updateStatus/{orderId}/{status}")
    public ResponseEntity<OrderDTO> update(@PathVariable Integer orderId,@PathVariable String status)
    {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,status));
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id)
    {
        orderService.deleteOrder(id);

        return ResponseEntity.ok("Delete Order Success!");

    }
}
