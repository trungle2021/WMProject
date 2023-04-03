package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.OrderDTO;

import com.springboot.wmproject.services.OrderService;
import com.springboot.wmproject.utils.SD;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.websocket.server.PathParam;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @GetMapping("/forVenue")

    public ResponseEntity<List<OrderDTO>> getAllForGetVenue()
    {
        return  ResponseEntity.ok(orderService.getAllForGetVenue());

    }
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byStatus/{status}")
    public ResponseEntity<List<OrderDTO>> getAllOrderByStatus(@PathVariable String status)
    {
        return  ResponseEntity.ok(orderService.getAllByOrderStatus(status));

    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/bycustomerd/{id}")
    public ResponseEntity<List<OrderDTO>> getAllOrderByCustomer(@PathVariable Integer id)
    {
        return  ResponseEntity.ok(orderService.getAllByCustomerId(id));

    }
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE','CUSTOMER','SALE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/bybookingEmp/{empId}")
    public ResponseEntity<List<OrderDTO>> getAllOrderbyBooking(@PathVariable Integer empId)
    {
        return  ResponseEntity.ok(orderService.getAllByBookingEmp(empId));

    }
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byTeam/empId/{empId}")
    public ResponseEntity<List<OrderDTO>> getAllOrderbyEmpTeam(@PathVariable Integer empId)
    {
        return  ResponseEntity.ok(orderService.getAllByTeamEmpId(empId));

    }
    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/byTeam/time/{teamId}/{month}/{year}")
    public ResponseEntity<List<OrderDTO>> getAllOrderbyTeamConfirm(@PathVariable Integer teamId,@PathVariable Integer month,@PathVariable Integer year)
    {
        List<OrderDTO>orderList=orderService.getAllByOrganizeTeam(teamId);
        if(orderList!=null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            List<OrderDTO> responseList = new ArrayList<>();
            for (OrderDTO obj : orderList) {
                LocalDateTime event = LocalDateTime.parse(obj.getTimeHappen(), formatter);
                if (event.getMonth().getValue() == month && event.getYear() == year && obj.getOrderStatus().equalsIgnoreCase(SD.orderStatusConfirm)) {
                    responseList.add(obj);
                }
            }
            return  ResponseEntity.ok(responseList);
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        }

    @PreAuthorize("hasAnyRole('ADMIN','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/have-shift-order")
    public ResponseEntity<List<OrderDTO>> getAllOrderConfirm()
    {

        return  ResponseEntity.ok(orderService.getAllOrderHaveShift());

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

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/updateStatus/{orderId}/{status}")
    public ResponseEntity<OrderDTO> update(@PathVariable Integer orderId,@PathVariable String status)
    {
        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,status));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/updateTable/{orderId}/{table}")
    public ResponseEntity<OrderDTO> updateOrderTable(@PathVariable Integer orderId,@PathVariable Integer table)
    {
        return ResponseEntity.ok(orderService.updateOrderTable(orderId,table));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update/changeTeam/{orderId}/{teamId}")
    public ResponseEntity<OrderDTO> updateOrderTeam(@PathVariable Integer orderId,@PathVariable Integer teamId)
    {
        return ResponseEntity.ok(orderService.updateOrderTeam(orderId,teamId));
    }
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER','SALE','ORGANIZE')")
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
        Integer table=order.getTableAmount();
        String contract= order.getContract();

        return ResponseEntity.ok(orderService.updateOrderStatus(orderId,status,bookingEmp,organizeTeam,orderTotal,part_time_emp_amount,table,contract));
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

    @PreAuthorize("hasAnyRole('ADMIN','SALE','CUSTOMER')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @PutMapping("/update/order-detail/customer")
    public ResponseEntity<String> updateOrderDetailCustomer(@RequestBody String json)
    {

        OrderDTO order= orderService.updateOrderDetailCustomer(json);
        if(order!=null) {

            return ResponseEntity.ok("Update order detail Success");
        }
        else{ return new ResponseEntity<> ("Update order fail!",HttpStatus.BAD_REQUEST);
        }

    }

}
