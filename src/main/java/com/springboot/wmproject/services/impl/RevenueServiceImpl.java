package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.*;
import com.springboot.wmproject.services.AuthServices.EmployeeService;
import com.springboot.wmproject.services.OrderService;
import com.springboot.wmproject.services.RevenueService;
import com.springboot.wmproject.utils.SD;
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
    private EmployeeService employeeService;

    @Autowired
    public RevenueServiceImpl(OrderService orderService,EmployeeService employeeService) {
        this.orderService = orderService;
        this.employeeService = employeeService;
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
                    if (order.getOrderStatus().equals(SD.orderStatusCanceled)) {
                        return 0.4 * order.getOrderTotal();
                    } else if (order.getOrderStatus().equals(SD.orderStatusCompleted)) {
                        return order.getOrderTotal();
                    } else if (order.getOrderStatus().equals(SD.orderStatusUncompleted)) {
                        return 0.4 * order.getOrderTotal();
                    } else if (order.getOrderStatus().equals(SD.orderStatusRefund)) {
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
    public ProfitMonthDTO getProfitByMonth(int year, int month) {
        Map<Integer,Integer> timeInput = new HashMap<>();
        timeInput.put(year,month);



        double totalOrderCostInMonth = 0;
        double totalCostEmpFullTimeInMonth = 0;
        double totalCostEmpPartTimeInMonth = 0;
        double totalRevenueInMonth = 0;
        double totalCostInMonth = 0;
        double totalProfitInMonth = 0;



        List<OrderDTO> orderDTOList = orderService.getAllOrder();

         totalOrderCostInMonth = orderDTOList.stream()
                .filter(orderDTO -> {
                    try {
                        return getYearAndMonth(orderDTO.getTimeHappen()).equals(timeInput);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .mapToDouble(order -> {

                    if (order.getOrderStatus().equals(SD.orderStatusCompleted)) {
                        double orderCost = (order.getCost() != null) ? order.getCost() : 0;
                        double partTimeAmount = (order.getPartTimeEmpAmount() != null) ? order.getPartTimeEmpAmount()*15 : 0;
                            return orderCost + partTimeAmount;
                    } else {
                        return 0.0;
                    }
                })
                .sum();

         totalCostEmpPartTimeInMonth = orderDTOList.stream()
                .filter(orderDTO -> {
                    try {
                        return getYearAndMonth(orderDTO.getTimeHappen()).equals(timeInput);
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                })
                .mapToDouble(order -> {
                    if (order.getOrderStatus().equals(SD.orderStatusCompleted)) {
                        double partTimeAmount = (order.getPartTimeEmpAmount() != null) ? order.getPartTimeEmpAmount()*15 : 0;
                        return partTimeAmount;
                    } else {
                        return 0.0;
                    }
                })
                .sum();


         totalCostEmpFullTimeInMonth = employeeService.getAllEmployees().stream().mapToDouble(
                emp ->  {
                    double empSalary = (emp.getSalary() != null) ? emp.getSalary() : 0;
                    return empSalary;
                }
        ).sum();

        totalCostInMonth = totalOrderCostInMonth + totalCostEmpFullTimeInMonth;
        totalRevenueInMonth = getRevenueByMonth(year,month).getRevenueInMonth();
        totalProfitInMonth = totalRevenueInMonth - totalCostInMonth;

        ProfitMonthDTO profitMonthDTO = new ProfitMonthDTO();
        profitMonthDTO.setMonth(month);
        profitMonthDTO.setYear(year);
        profitMonthDTO.setCostEmpFullTimeInMonth(totalCostEmpFullTimeInMonth);
        profitMonthDTO.setCostEmpPartTimeInMonth(totalCostEmpPartTimeInMonth);
        profitMonthDTO.setCostOrderInMonth(totalOrderCostInMonth);
        profitMonthDTO.setTotalProfitInMonth(totalProfitInMonth);
        profitMonthDTO.setTotalCostInMonth(totalCostInMonth);
        profitMonthDTO.setTotalRevenueInMonth(totalRevenueInMonth);

        return profitMonthDTO;
    }

    @Override
    public RevenueYearDTO getRevenueByYear(int year) {
        YearMonth currentYearMonth = YearMonth.now();
        Month currentMonth = currentYearMonth.getMonth();
        int currentMonthValue = currentMonth.getValue();

        RevenueYearDTO result = new RevenueYearDTO();
        List<RevenueMonthDTO> revenueByYear = new ArrayList<>();
        for (int i = 1; i <= currentMonthValue; i++) {
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
    public ProfitYearDTO getProfitByYear(int year) {
        YearMonth currentYearMonth = YearMonth.now();
        Month currentMonth = currentYearMonth.getMonth();
        int currentMonthValue = currentMonth.getValue();

        ProfitYearDTO result = new ProfitYearDTO();
        List<ProfitMonthDTO> profitMonthList = new ArrayList<>();
        for (int i = 1; i <= currentMonthValue; i++) {
            ProfitMonthDTO profitByMonth = getProfitByMonth(year,i);
            profitMonthList.add(profitByMonth);
        }

        double totalProfitInYear = profitMonthList.stream()
                .mapToDouble(profitMonthDTO -> profitMonthDTO.getTotalProfitInMonth())
                .sum() ;


        result.setProfitMonthList(profitMonthList);
        result.setTotalProfitInYear(totalProfitInYear);
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

        int countOrderCompleted = orderStatusCounts.containsKey(SD.orderStatusCompleted) ? orderStatusCounts.get(SD.orderStatusCompleted).intValue() : 0;
        int countOrderRefunded = orderStatusCounts.containsKey(SD.orderStatusRefund) ? orderStatusCounts.get(SD.orderStatusRefund).intValue() : 0;
        int countOrderCanceled = orderStatusCounts.containsKey(SD.orderStatusCanceled)? orderStatusCounts.get(SD.orderStatusCanceled).intValue() : 0;
        int countOrderUnCompleted = orderStatusCounts.containsKey(SD.orderStatusUncompleted) ? orderStatusCounts.get(SD.orderStatusUncompleted).intValue() : 0;



        OrderInMonthDTO orderInMonthDTO = new OrderInMonthDTO();
        orderInMonthDTO.setMonth(month);
        orderInMonthDTO.setYear(year);
        orderInMonthDTO.setCountOrderCompleted(countOrderCompleted);
        orderInMonthDTO.setCountOrderRefunded(countOrderRefunded);
        orderInMonthDTO.setCountOrderCanceled(countOrderCanceled);
        orderInMonthDTO.setCountOrderUnCompleted(countOrderUnCompleted);

        return orderInMonthDTO;
    }

    @Override
    public OrderIn3MonthDTO getOrderCount3Month(int year) {
        int[] threeMonthsRecent;
        OrderIn3MonthDTO result;
        YearMonth currentYearMonth = YearMonth.now();
        Month currentMonth = currentYearMonth.getMonth();
        int currentMonthValue = currentMonth.getValue();
        int oneBeforeCurrentMonth = currentMonthValue - 1;
        int twoBeforeCurrentMonth = currentMonthValue - 2;


        if(currentMonthValue == 1){
            //currentMonth is 1
            List<OrderInMonthDTO> orderIn3Month = new ArrayList<>();

            OrderInMonthDTO orderInMonth = getOrderCountByMonth(year,currentMonthValue);
            orderIn3Month.add(orderInMonth);
            orderInMonth = getOrderCountByMonth(year-1,12);
            orderIn3Month.add(orderInMonth);
            orderInMonth = getOrderCountByMonth(year-1,11);
            orderIn3Month.add(orderInMonth);

            int[] totals = orderIn3Month.stream()
                    .reduce(new int[4],
                            (acc, order) -> {
                                acc[0] += order.getCountOrderCompleted();
                                acc[1] += order.getCountOrderRefunded();
                                acc[2] += order.getCountOrderCanceled();
                                acc[3] += order.getCountOrderUnCompleted();
                                return acc;
                            },
                            (acc1, acc2) -> {
                                acc1[0] += acc2[0];
                                acc1[1] += acc2[1];
                                acc1[2] += acc2[2];
                                acc1[3] += acc2[3];
                                return acc1;
                            });

            int totalOrderCompleted = totals[0];
            int totalOrderRefunded = totals[1];
            int totalOrderCanceled = totals[2];
            int totalOrderUnCompleted = totals[3];

             result = new OrderIn3MonthDTO();
            result.setOrderIn3Month(orderIn3Month);
            result.setTotalOrderCompleted(totalOrderCompleted);
            result.setTotalOrderRefunded(totalOrderRefunded);
            result.setTotalOrderCanceled(totalOrderCanceled);
            result.setTotalOrderUncompleted(totalOrderUnCompleted);
            return result;

        }else if(currentMonthValue == 2){
            List<OrderInMonthDTO> orderIn3Month = new ArrayList<>();

            OrderInMonthDTO orderInMonth = getOrderCountByMonth(year,currentMonthValue);
            orderIn3Month.add(orderInMonth);
            orderInMonth = getOrderCountByMonth(year-1,1);
            orderIn3Month.add(orderInMonth);
            orderInMonth = getOrderCountByMonth(year-1,12);
            orderIn3Month.add(orderInMonth);

            int[] totals = orderIn3Month.stream()
                    .reduce(new int[4],
                            (acc, order) -> {
                                acc[0] += order.getCountOrderCompleted();
                                acc[1] += order.getCountOrderRefunded();
                                acc[2] += order.getCountOrderCanceled();
                                acc[3] += order.getCountOrderUnCompleted();
                                return acc;
                            },
                            (acc1, acc2) -> {
                                acc1[0] += acc2[0];
                                acc1[1] += acc2[1];
                                acc1[2] += acc2[2];
                                acc1[3] += acc2[3];
                                return acc1;
                            });

            int totalOrderCompleted = totals[0];
            int totalOrderRefunded = totals[1];
            int totalOrderCanceled = totals[2];
            int totalOrderUnCompleted = totals[3];

            result = new OrderIn3MonthDTO();
            result.setOrderIn3Month(orderIn3Month);
            result.setTotalOrderCompleted(totalOrderCompleted);
            result.setTotalOrderRefunded(totalOrderRefunded);
            result.setTotalOrderCanceled(totalOrderCanceled);
            result.setTotalOrderUncompleted(totalOrderUnCompleted);
            return result;

        }else{
            threeMonthsRecent = new int[]{twoBeforeCurrentMonth,oneBeforeCurrentMonth, currentMonthValue};
            List<OrderInMonthDTO> orderIn3Month = new ArrayList<>();

            for (int i = 0; i < threeMonthsRecent.length; i++) {
                OrderInMonthDTO orderInMonth = getOrderCountByMonth(year,threeMonthsRecent[i]);
                orderIn3Month.add(orderInMonth);
            }

            int[] totals = orderIn3Month.stream()
                    .reduce(new int[4],
                            (acc, order) -> {
                                acc[0] += order.getCountOrderCompleted();
                                acc[1] += order.getCountOrderRefunded();
                                acc[2] += order.getCountOrderCanceled();
                                acc[3] += order.getCountOrderUnCompleted();
                                return acc;
                            },
                            (acc1, acc2) -> {
                                acc1[0] += acc2[0];
                                acc1[1] += acc2[1];
                                acc1[2] += acc2[2];
                                acc1[3] += acc2[3];
                                return acc1;
                            });

            int totalOrderCompleted = totals[0];
            int totalOrderRefunded = totals[1];
            int totalOrderCanceled = totals[2];
            int totalOrderUnCompleted = totals[3];

            result = new OrderIn3MonthDTO();
            result.setOrderIn3Month(orderIn3Month);
            result.setTotalOrderCompleted(totalOrderCompleted);
            result.setTotalOrderRefunded(totalOrderRefunded);
            result.setTotalOrderCanceled(totalOrderCanceled);
            result.setTotalOrderUncompleted(totalOrderUnCompleted);
            return result;
        }
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
