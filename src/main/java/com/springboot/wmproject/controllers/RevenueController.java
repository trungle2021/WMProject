package com.springboot.wmproject.controllers;

import com.springboot.wmproject.DTO.*;
import com.springboot.wmproject.services.RevenueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/revenues")
public class RevenueController {
    public RevenueService revenueService;
    @Autowired
    public RevenueController(RevenueService revenueService) {
        this.revenueService = revenueService;
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/getRevenueByMonth/{year}/{month}")
    public ResponseEntity<RevenueMonthDTO> getRevenueByMonth(@PathVariable("year") int year, @PathVariable("month") int month){
        RevenueMonthDTO revenueByMonth = revenueService.getRevenueByMonth(year,month);
        return ResponseEntity.ok(revenueByMonth);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/getOrderCountByMonth/{year}/{month}")
    public ResponseEntity<OrderInMonthDTO> getOrderCountByMonth(@PathVariable("year") int year, @PathVariable("month") int month){
        OrderInMonthDTO orderCountByMonth = revenueService.getOrderCountByMonth(year,month);
        return ResponseEntity.ok(orderCountByMonth);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/getOrderCount3Month/{year}")
    public ResponseEntity<OrderIn3MonthDTO> getOrderCount3Month(@PathVariable("year") int year){
        OrderIn3MonthDTO orderIn3MonthDTO = revenueService.getOrderCount3Month(year);
        return ResponseEntity.ok(orderIn3MonthDTO);
    }


    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/getRevenueByYear/{year}")
    public ResponseEntity<RevenueYearDTO> getRevenueByYear(@PathVariable("year") int year){
        RevenueYearDTO revenueByYear = revenueService.getRevenueByYear(year);
        return ResponseEntity.ok(revenueByYear);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/getProfitByYear/{year}")
    public ResponseEntity<ProfitYearDTO> getProfitByYear(@PathVariable("year") int year){
        ProfitYearDTO profitByYear = revenueService.getProfitByYear(year);
        return ResponseEntity.ok(profitByYear);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SALE','ORGANIZE')")
    @Operation(summary = "My endpoint", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping(value = "/getProfitByMonth/{year}/{month}")
    public ResponseEntity<ProfitMonthDTO> getProfitByMonth(@PathVariable("year") int year, @PathVariable("month") int month){
        ProfitMonthDTO profitByMonth = revenueService.getProfitByMonth(year,month);
        return ResponseEntity.ok(profitByMonth);
    }

}
