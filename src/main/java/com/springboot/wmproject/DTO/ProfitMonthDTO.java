package com.springboot.wmproject.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProfitMonthDTO {
    private int month;
    private int year;
    private Double costOrderInMonth;
    private Double costEmpPartTimeInMonth;
    private Double costEmpFullTimeInMonth;
    private Double totalProfitInMonth;
    private Double totalCostInMonth;
    private Double totalRevenueInMonth;
}
