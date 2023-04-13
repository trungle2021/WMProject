package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.OrderIn3MonthDTO;
import com.springboot.wmproject.DTO.OrderInMonthDTO;
import com.springboot.wmproject.DTO.RevenueMonthDTO;
import com.springboot.wmproject.DTO.RevenueYearDTO;

import java.util.List;
import java.util.Map;

public interface RevenueService {
    public RevenueMonthDTO getRevenueByMonth(int year, int month);
    public RevenueYearDTO getRevenueByYear(int year);

    public OrderInMonthDTO getOrderCountByMonth(int year, int month);
    public OrderIn3MonthDTO getOrderCount3Month(int year);
}
