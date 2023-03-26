package com.springboot.wmproject.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.DTO.OrderDTO;
import com.springboot.wmproject.entities.*;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.*;
import com.springboot.wmproject.services.OrderService;
import com.springboot.wmproject.utils.EmailSender;
import com.springboot.wmproject.utils.MailContent;
import com.springboot.wmproject.utils.SD;
import jakarta.mail.MessagingException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.springboot.wmproject.utils.SD.*;

@Service
public class OrderServiceImpl implements OrderService {
     private OrderRepository orderRepository;
    private FoodDetailRepository fRepository;
    private ServiceDetailRepository sRepository;
    private VenueRepository venueRepository;
    private CustomerRepository customerRepository;
    private EmailSender mailSender;

    private ModelMapper modelMapper;

    private EmployeeRepository employeeRepository;
    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, FoodDetailRepository fRepository, ServiceDetailRepository sRepository, VenueRepository venueRepository, CustomerRepository customerRepository, EmailSender mailSender, ModelMapper modelMapper, EmployeeRepository employeeRepository) {
        this.orderRepository = orderRepository;
        this.fRepository = fRepository;
        this.sRepository = sRepository;
        this.venueRepository = venueRepository;
        this.customerRepository = customerRepository;
        this.mailSender = mailSender;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
    }




    @Override
    public List<OrderDTO> getAllOrder() throws ResourceNotFoundException {
        return orderRepository.findAll().stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllByOrderDate(String orderDate) throws ResourceNotFoundException {
        List<OrderDTO> orders = new ArrayList<>();
            if (orderDate != null) {
                 orders = orderRepository.findByOrderDate(orderDate).stream().map(order -> mapToDTO(order)).collect(Collectors.toList());
                if (orders.isEmpty()) {
                    return null;
                }
            }
        return orders;

    }

    @Override
    public List<OrderDTO> getAllByTimeHappen(String time) throws ResourceNotFoundException {
        if(time!=null){
            List<OrderDTO> orderDTO=orderRepository.findByTimeHappen(time).stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
            if(!orderDTO.isEmpty()){
                return orderDTO;
            }
        }
        return null;
    }

    @Override
    public List<OrderDTO> getAllByVenueId(int id)throws ResourceNotFoundException {
        if(id!=0){
            List<OrderDTO> orderDTOS=orderRepository.findByVenueId(id).stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
            if(!orderDTOS.isEmpty()){
                return orderDTOS;
            }
        }
        return null;
    }

    @Override
    public List<OrderDTO> getAllByBookingEmp(int id)throws ResourceNotFoundException {
        if(id!=0){
            List<OrderDTO> orderDTOS=orderRepository.findByBookingEmp(id).stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
            if(!orderDTOS.isEmpty()){
                return orderDTOS;
            }
        }
        return null;
    }

    @Override
    public List<OrderDTO> getAllByOrganizeTeam(int id)throws ResourceNotFoundException {
        if(id!=0){
            List<OrderDTO> orderDTOS=orderRepository.findByOrganizeTeamId(id).stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
            if(!orderDTOS.isEmpty()){
                return orderDTOS;
            }
        }
        return null;
    }

    @Override
    public List<OrderDTO> getAllByTeamEmpId(int id) {
        if(id!=0){
            List<OrderDTO> orderDTOS=orderRepository.findAll().stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
            Employees emp=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employees","Id",String.valueOf(id)));
            Integer team=emp.getOrganizeTeamsByTeamId().getId();
           List<OrderDTO>list= new ArrayList<>();
            for (OrderDTO order:orderDTOS)
            {
             if(order.getOrganizeTeam()==team && order.getOrderStatus().equalsIgnoreCase(orderStatusConfirm))
             {
                list.add(order);
             }

            }
                return list;
        }
        return null;
    }

    @Override
    public List<OrderDTO> getAllByCustomerId(int id)throws ResourceNotFoundException {
        if(id!=0){
            List<OrderDTO> orderDTOS=orderRepository.findByCustomerId(id).stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
            if(!orderDTOS.isEmpty()){
                return orderDTOS;
            }
        }
        return null;
    }

    @Override
    public List<OrderDTO> getAllByOrderStatus(String status)throws ResourceNotFoundException {
        if(status!=null){
            List<OrderDTO> orderDTOS=orderRepository.findByOrderStatus(status).stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
            if(!orderDTOS.isEmpty()){
                return orderDTOS;
            }
        }
        return null;
    }

    @Override
    public OrderDTO getOneOrderByOrderId(int id)throws ResourceNotFoundException {
        if(id!=0){
            Orders order=orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order","Id",String.valueOf(id)));
            if(order!=null){
                return mapToDTO(order);
            }
        }
        return null;
    }

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        if(orderDTO!=null){
            String timeHappen=orderDTO.getTimeHappen();
            int venueId= orderDTO.getVenueId();
            //check if order has the same time and venue//sai logic
            List<Orders> orderList=orderRepository.orderListInTimeVenue(timeHappen,venueId);
            //if not create new
            boolean booked=false;

            for (Orders order:orderList)
            {
             if(order.getOrderStatus().equalsIgnoreCase(orderStatusOrdered)||order.getOrderStatus().equalsIgnoreCase(orderStatusDeposited)
             || order.getOrderStatus().equalsIgnoreCase(orderStatusConfirm)||order.getOrderStatus().equalsIgnoreCase(orderStatusWarning))
             {
                booked=true;
                break;
             }
            }
            if(!booked) {
                Orders orders = orderRepository.save(mapToEntity(orderDTO));
                Orders newOrder = orderRepository.findById(orders.getId()).orElseThrow(()->new ResourceNotFoundException("Order","Id",String.valueOf(orders.getId())));
            //set cung
                Venues venues=venueRepository.findById(newOrder.getVenueId()).orElseThrow(() -> new ResourceNotFoundException("venueId","id",String.valueOf(newOrder.getVenueId())));
                Customers  customeObject=customerRepository.findById(newOrder.getCustomerId()).orElseThrow(() -> new ResourceNotFoundException("customerId","id",String.valueOf(newOrder.getCustomerId())));

//                String to="khangkhangbl@gmail.com";
                String to= customeObject.getEmail();
                if(to==null){to="khangkhangbl@gmail.com";}

                String company="KTK-Wedding";
                String customer="Customer " +customeObject.getFirst_name();
                String content= MailContent.getContent(customer,String.valueOf(newOrder.getId()),venues.getVenueName(),
                        newOrder.getOrderDate(),newOrder.getTimeHappen(),
                        newOrder.getOrderStatus(),company);
                try {
                    mailSender.sendEmail(to,"Your order booking success", content);
                    } catch (MessagingException e) {

                    throw new RuntimeException(e);
                }

                //send mail
                return mapToDTO(newOrder);
            }
            else{
                return null;
            }
        }
        return null;
    }

    @Override
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        int orderId=orderDTO.getId();
        if(orderId!=0){
            Orders orders=orderRepository.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderId)));
            if(orders!=null){
//                orders.setId(orderDTO.getId());
                orders.setOrderDate(orderDTO.getOrderDate());
                orders.setOrderStatus(orderDTO.getOrderStatus());
                orders.setOrderTotal(orderDTO.getOrderTotal());
                orders.setTimeHappen(orderDTO.getTimeHappen());
                orders.setVenueId(orderDTO.getVenueId());
                orders.setBookingEmp(orderDTO.getBookingEmp());
                orders.setOrganizeTeam(orderDTO.getOrganizeTeam());
                orders.setCustomerId(orderDTO.getCustomerId());
                orders.setTableAmount(orderDTO.getTableAmount());
                orders.setPartTimeEmpAmount(orderDTO.getPartTimeEmpAmount());
                orderRepository.save(orders);
                return mapToDTO(orders);
            }
        }
        return null;
    }
    @Override
    public OrderDTO updateOrderStatus(Integer orderDTOId,String status) {

        if(orderDTOId!=0){
            Orders orders=orderRepository.findById(orderDTOId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderDTOId)));
            if(orders!=null){

                orders.setOrderStatus(status);
                if(status.equalsIgnoreCase(orderStatusRefund))
                {
//                    String to="khangkhangbl@gmail.com";
                String to= orders.getCustomersByCustomerId().getEmail();
                if(to==null){to="khangkhangbl@gmail.com";}

                    String company="KTK-Wedding";
                    String customer="Customer " +orders.getCustomersByCustomerId().getFirst_name();
                    String content= MailContent.getRefundMail(customer,orders.getVenues().getVenueName(),orders.getOrderDate(),orders.getTimeHappen(),String.valueOf(orders.getOrderTotal()/20));
                    try {
                        mailSender.sendEmail(to,"Your canceling request accepted!", content);
                    } catch (MessagingException e) {

                        throw new RuntimeException(e);
                    }
                }

                orderRepository.save(orders);
                return mapToDTO(orders);
            }
        }
        return null;
    }

    @Override
    public OrderDTO updateOrderStatus(Integer orderDTOId, String status, Integer bookingEmp, Integer organizeTeam, Double orderTotal, Integer part_time_emp_amount,Integer table,String contract) {

        if(orderDTOId!=0){
            Orders orders=orderRepository.findById(orderDTOId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderDTOId)));
            if(orders!=null){

                orders.setOrderStatus(status);
                orders.setBookingEmp(bookingEmp);
                orders.setOrganizeTeam(organizeTeam);
                orders.setOrderTotal(orderTotal);
                orders.setPartTimeEmpAmount(part_time_emp_amount);
                orders.setTableAmount(table);
                orders.setContract(contract);

                orderRepository.save(orders);
                return mapToDTO(orders);
            }
        }
        return null;
    }

    @Override
    public OrderDTO updateOrderTable(Integer orderDTOId, Integer table) {
        if(orderDTOId!=0){
            Orders orders=orderRepository.findById(orderDTOId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderDTOId)));
            if(orders!=null){

                orders.setTableAmount(table);

                orderRepository.save(orders);
                return mapToDTO(orders);
            }
        }
        return null;
    }
//nho transactional for delete query
    @Override
    @Transactional
    public OrderDTO updateOrderDetailCustomer(String json) {

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String,Object> data= objectMapper.readValue(json, new TypeReference<Map<String, Object>>() {});

            List<String> foodData=objectMapper.readValue(objectMapper.writeValueAsString(data.get("foodList")), new TypeReference< List<String>>() {});

            List<String> svData=objectMapper.readValue(objectMapper.writeValueAsString(data.get("serviceList")), new TypeReference<List<String>>() {});


            Integer orderId= Integer.parseInt((data.get("orderId")).toString());
            Integer tableAmount= Integer.parseInt(data.get("table").toString());

            Orders order= orderRepository.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("OrderId","id",String.valueOf(orderId)));
            order.setTableAmount(tableAmount);
            orderRepository.save(order);
           fRepository.deleteByOrderId(orderId);

            sRepository.deleteByOrderId(orderId);
            //delete all food and service detail of order.


            //add new fd and order by food Id,

//            newFoodDetail.setFoodId(Integer.parseInt(foodId));
//            newFoodDetail.setOrderId(orderId);
            if(foodData!=null) {
            for (String foodId:foodData)
            {
                FoodDetails newFoodDetail= new FoodDetails();
                newFoodDetail.setFoodId(Integer.parseInt(foodId));
                newFoodDetail.setOrderId(orderId);
                fRepository.save(newFoodDetail);
            }
            }

            if(svData!=null) {
                //service
                for (String svId : svData) {
                    ServiceDetails newServiceDetail = new ServiceDetails();
                    newServiceDetail.setServiceId(Integer.parseInt(svId));
                    newServiceDetail.setOrderId(orderId);
                    sRepository.save(newServiceDetail);
                }
            }
                        //lay orderdetail

        return mapToDTO(order);


        } catch (Exception e) {
//            throw new RuntimeException(e);
                 return null;
        }



    }





    @Override
    public void deleteOrder(int id) {
        Orders orders=orderRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(id)));
        orderRepository.delete(orders);
    }

    public OrderDTO mapToDTO(Orders order) {
        OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
        return orderDTO;

    }


    public Orders mapToEntity(OrderDTO orderDTO) {
        Orders order = modelMapper.map(orderDTO, Orders.class);
        return order;

    }

}

