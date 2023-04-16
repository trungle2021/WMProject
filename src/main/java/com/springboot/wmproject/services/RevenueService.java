package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.*;

import java.util.List;
import java.util.Map;

public interface RevenueService {
    public RevenueMonthDTO getRevenueByMonth(int year, int month);
    public ProfitMonthDTO getProfitByMonth(int year, int month);
    public RevenueYearDTO getRevenueByYear(int year);
    public ProfitYearDTO getProfitByYear(int year);

    public OrderInMonthDTO getOrderCountByMonth(int year, int month);
    public OrderIn3MonthDTO getOrderCount3Month(int year);
}
