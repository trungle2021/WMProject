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
@RequestMapping("api/orders")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping

    public ResponseEntity<List<OrderDTO>> getAllOrder()
    {
        return  ResponseEntity.ok(orderService.getAllOrder());

    }
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/bybookingEmp/{empId}")
    public ResponseEntity<List<OrderDTO>> getAllOrderbyBooking(@PathVariable Integer empId)
    {
        return  ResponseEntity.ok(orderService.getAllByBookingEmp(empId));

    }
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byTeam/{teamId}")
    public ResponseEntity<List<OrderDTO>> getAllOrderbyTeam(@PathVariable Integer teamId)
    {
        return  ResponseEntity.ok(orderService.getAllByOrganizeTeam(teamId));

    }

//    @PreAuthorize("hasAnyRole('ADMIN','EMPLOYEE')")
//    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    public ResponseEntity<OrderDTO> create(@RequestBody OrderDTO order)
    {
        return new ResponseEntity<>(orderService.createOrder(order), HttpStatus.CREATED);
    }


    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update")
    public ResponseEntity<OrderDTO> update(@RequestBody OrderDTO order)
    {
        return ResponseEntity.ok(orderService.updateOrder(order));


    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/updateStatus/{orderId}/{status}")
    public ResponseEntity<OrderDTO> update(@PathVariable Integer orderId,@PathVariable String status)
    {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,status));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/updateTable/{orderId}/{table}")
    public ResponseEntity<OrderDTO> update(@PathVariable Integer orderId,@PathVariable Integer table)
    {
        return ResponseEntity.ok(orderService.updateOrderTable(orderId,table));
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/updateStatus")
    public ResponseEntity<OrderDTO> updateStatus(@RequestBody OrderDTO order)
    {
        Integer orderId=order.getId();
        String status=order.getOrderStatus();
        Integer bookingEmp=order.getBookingEmp();
        Integer organizeTeam=order.getOrganizeTeam();
        Double orderTotal=order.getOrderTotal();
        Integer part_time_emp_amount=order.getPartTimeEmpAmount();

        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,status,bookingEmp,organizeTeam,orderTotal,part_time_emp_amount));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id)
    {
        orderService.deleteOrder(id);

        return ResponseEntity.ok("Delete Order Success!");

    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOneOrder(@PathVariable Integer id)
    {
        return ResponseEntity.ok(orderService.getOneOrderByOrderId(id));
    }
}
