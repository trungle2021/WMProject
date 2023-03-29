package com.springboot.wmproject.services.impl;

import com.google.gson.Gson;
import com.springboot.wmproject.DTO.*;
import com.springboot.wmproject.entities.Orders;
import com.springboot.wmproject.services.OrderService;
import com.springboot.wmproject.services.RevenueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Month;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RevenueServiceImpl implements RevenueService {
    private OrderService orderService;

    @Autowired
    public RevenueServiceImpl(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public RevenueMonthDTO getRevenueByMonth(int year, int month) {
        Map<Integer,Integer> timeInput = new HashMap<>();
        timeInput.put(year,month);


        List<OrderDTO> orderDTOList = orderService.getAllOrder();


        double totalOrderAmount = orderDTOList.stream()
                .filter(orderDTO -> {
                    try {
                        return getYearAndMonth(orderDTO.getTimeHappen()).equals(timeInput);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .mapToDouble(order -> {
                    if (order.getOrderStatus().equals("canceled")) {
                        return 0.4 * order.getOrderTotal();
                    } else if (order.getOrderStatus().equals("completed")) {
                        return order.getOrderTotal();
                    } else if (order.getOrderStatus().equals("uncompleted")) {
                        return 0.4 * order.getOrderTotal();
                    } else if (order.getOrderStatus().equals("refund")) {
                        return 0.1 * order.getOrderTotal();
                    } else {
                        return 0.0;
                    }
                })
                .sum();
        RevenueMonthDTO revenueMonthDTO = new RevenueMonthDTO();
        revenueMonthDTO.setMonth(month);
        revenueMonthDTO.setYear(year);
        revenueMonthDTO.setRevenueInMonth(totalOrderAmount);
        return revenueMonthDTO;
    }

    @Override
    public RevenueYearDTO getRevenueByYear(int year) {
        int[] monthInYear = {1,2,3,4,5,6,7,8,9,10,11,12};
        RevenueYearDTO result = new RevenueYearDTO();
        List<RevenueMonthDTO> revenueByYear = new ArrayList<>();
        for (int i = 1; i <= monthInYear.length; i++) {
           RevenueMonthDTO revenueByMonth = getRevenueByMonth(year,i);
            revenueByYear.add(revenueByMonth);
        }
        double totalRevenueInYear = revenueByYear.stream()
                .mapToDouble(revenueInMonth -> revenueInMonth.revenueInMonth)
                .sum();

        result.setRevenueMonthList(revenueByYear);
        result.setTotalRevenueYear(totalRevenueInYear);
        return result;
    }

    @Override
    public OrderInMonthDTO getOrderCountByMonth(int year, int month) {
        Map<Integer,Integer> timeInput = new HashMap<>();
        timeInput.put(year,month);


        List<OrderDTO> orderDTOList = orderService.getAllOrder();


        Map<String, Long> orderStatusCounts = orderDTOList.stream()
                .filter(orderDTO -> {
                    try {
                        return getYearAndMonth(orderDTO.getTimeHappen()).equals(timeInput);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.groupingBy(OrderDTO::getOrderStatus, Collectors.counting()));

        OrderInMonthDTO orderInMonthDTO = new OrderInMonthDTO();
        orderInMonthDTO.setMonth(month);
        orderInMonthDTO.setYear(year);
        orderInMonthDTO.setCountOrderCompleted(orderStatusCounts.get("completed").intValue());
        orderInMonthDTO.setCountOrderRefund(orderStatusCounts.get("refund").intValue());
        orderInMonthDTO.setCountOrderCancel(orderStatusCounts.get("cancel").intValue());
        orderInMonthDTO.setCountOrderUnCompleted(orderStatusCounts.get("uncompleted").intValue());

        return orderInMonthDTO;
    }

    @Override
    public OrderIn3MonthDTO getOrderCount3Month(int year) {
        int[] threeMonthsRecent = new int[3];
        YearMonth currentYearMonth = YearMonth.now();
        Month currentMonth = currentYearMonth.getMonth();
        int currentMonthValue = currentMonth.getValue();
        int oneBeforeCurrentMonth = currentMonthValue - 1;
        int twoBeforeCurrentMonth = currentMonthValue - 2;
        if(oneBeforeCurrentMonth == 0 && twoBeforeCurrentMonth <0){
            //currentMonth is 1
            threeMonthsRecent = new int[]{currentMonthValue};
        }else if(oneBeforeCurrentMonth == 1 && twoBeforeCurrentMonth ==0){
             threeMonthsRecent = new int[]{oneBeforeCurrentMonth, currentMonthValue};
        }else{
            threeMonthsRecent = new int[]{oneBeforeCurrentMonth, currentMonthValue};
        }

        ///// CHUAW LAM XONG

        RevenueYearDTO result = new RevenueYearDTO();
        List<RevenueMonthDTO> revenueByYear = new ArrayList<>();
        for (int i = 0; i < threeMonthsRecent.length; i++) {
            RevenueMonthDTO revenueByMonth = getRevenueByMonth(year,i);
            revenueByYear.add(revenueByMonth);
        }
        double totalRevenueInYear = revenueByYear.stream()
                .mapToDouble(revenueInMonth -> revenueInMonth.revenueInMonth)
                .sum();

        result.setRevenueMonthList(revenueByYear);
        result.setTotalRevenueYear(totalRevenueInYear);
        return null;
    }

    public Map<Integer, Integer> getYearAndMonth(String dateString) throws ParseException {
        Map<Integer, Integer> yearAndMonth = new HashMap<>();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = dateFormat.parse(dateString);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1; // Add 1 because Calendar.MONTH is zero-based

        yearAndMonth.put(year, month);
        return yearAndMonth;
    }





}
