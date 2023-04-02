package com.springboot.wmproject.services.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.DTO.OrderDTO;
import com.springboot.wmproject.DTO.OrganizeTeamDTO;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private OrganizeTeamRepository teamRepository;
    private EmailSender mailSender;

    private ModelMapper modelMapper;

    private EmployeeRepository employeeRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, FoodDetailRepository fRepository, ServiceDetailRepository sRepository, VenueRepository venueRepository, CustomerRepository customerRepository, OrganizeTeamRepository teamRepository, EmailSender mailSender, ModelMapper modelMapper, EmployeeRepository employeeRepository) {
        this.orderRepository = orderRepository;
        this.fRepository = fRepository;
        this.sRepository = sRepository;
        this.venueRepository = venueRepository;
        this.customerRepository = customerRepository;
        this.teamRepository = teamRepository;
        this.mailSender = mailSender;
        this.modelMapper = modelMapper;
        this.employeeRepository = employeeRepository;
    }





    @Override
    public List<OrderDTO> getAllOrder() throws ResourceNotFoundException {
        return orderRepository.findAll().stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getAllOrderHaveShift() {
        List<OrderDTO> confirmList=orderRepository.findByOrderStatus(orderStatusConfirm).stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
        List<OrderDTO> completedList=orderRepository.findByOrderStatus(orderStatusCompleted).stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
        List<OrderDTO> uncompletedList=orderRepository.findByOrderStatus(orderStatusUncompleted).stream().map(orders -> mapToDTO(orders)).collect(Collectors.toList());
        List<OrderDTO> responseList=new ArrayList<>();
        responseList.addAll(confirmList);
        responseList.addAll(completedList);
        responseList.addAll(uncompletedList);
        if(responseList!=null)
        {return responseList;}
        else {return null;}
    }

//    @Override
//    public List<OrderDTO> getAllOrderConfirm() {
//       List<Orders> listConfirm= orderRepository.findAll();
//       listConfirm.stream().filter(order->order.getOrderStatus().equalsIgnoreCase(orderStatusConfirm)).collect(Collectors.toList());
//       return listConfirm.stream().map(orders->mapToDTO(orders)).collect(Collectors.toList());;
//    }


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
                if(to==null|| to.isEmpty()){to="khangkhangbl@gmail.com";}

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
                if(to==null|| to.isEmpty()){to="khangkhangbl@gmail.com";}

                    String company="KTK-Wedding";
                    String customer="Customer " +orders.getCustomersByCustomerId().getFirst_name();
                    String content= MailContent.getRefundMail(customer,orders.getVenues().getVenueName(),orders.getOrderDate(),orders.getTimeHappen(),String.valueOf(orders.getOrderTotal()/20));
                    try {
                        mailSender.sendEmail(to,"Your canceling request accepted!", content);
                        orderRepository.save(orders);
                    } catch (MessagingException e) {

                        throw new RuntimeException(e);
                    }
                }



                return mapToDTO(orders);
            }
        }
        throw new WmAPIException(HttpStatus.BAD_REQUEST,"OrderId required!");
    }

    @Override
    public OrderDTO updateOrderStatus(Integer orderDTOId, String status, Integer bookingEmp, Integer organizeTeam, Double orderTotal, Integer part_time_emp_amount,Integer table,String contract) {

        if(orderDTOId!=0){
            Orders orders=orderRepository.findById(orderDTOId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderDTOId)));
            //check xem co order food detail chua.

            if(orders!=null){
                orders.setOrderStatus(status);
                if(status.equalsIgnoreCase(orderStatusConfirm))
                {
                    //set cost if status is confirm
                    orders.setCost(getTotalCostofOrder(orders,table));
                }
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
        throw new WmAPIException(HttpStatus.BAD_REQUEST,"OrderId required!");
    }

    @Override
    public OrderDTO updateOrderTable(Integer orderDTOId, Integer table) {
        if(orderDTOId!=0){
            Orders order=orderRepository.findById(orderDTOId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderDTOId)));
            if(order!=null){
                order.setTableAmount(table);
                orderRepository.save(order);
                return mapToDTO(order);
            }
        }
        throw new WmAPIException(HttpStatus.BAD_REQUEST,"OrderId required!");
    }

    @Override
    public OrderDTO updateOrderTeam(Integer orderDTOId, Integer teamId) {
       if(orderDTOId!=0)
       {
           OrganizeTeams myTeam= teamRepository.findById(teamId).orElseThrow(()->new ResourceNotFoundException("Team","id",String.valueOf(teamId)));
           Orders order=orderRepository.findById(orderDTOId).orElseThrow(()->new ResourceNotFoundException("Order","id",String.valueOf(orderDTOId)));
          if(myTeam==null)
          {
           throw new WmAPIException(HttpStatus.BAD_REQUEST,"Team not found!");
          }

          //check team admin
          if(myTeam.getTeamName().equalsIgnoreCase(TEAM_ADMINISTRATOR))
          {
              throw new WmAPIException(HttpStatus.BAD_REQUEST,"Cant choose ADMINISTRATOR team");
          }
           if(order!=null && order.getOrderStatus().equalsIgnoreCase(orderStatusConfirm) && order.getOrganizeTeam()!=teamId) {
               //change partime emp
               DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
               LocalDateTime event=LocalDateTime.parse(order.getTimeHappen(),formatter);
              LocalDateTime now= LocalDateTime.now();
              if(now.isAfter(event)){
                  throw new WmAPIException(HttpStatus.BAD_REQUEST,"You can't change the shift after event organized!");
              }

               order.setPartTimeEmpAmount(getPartimeEmp(teamId, order.getTableAmount()));
               order.setOrganizeTeam(teamId);
              //CACULATE cost again

               //
               orderRepository.save(order);
               return mapToDTO(order);
           }
           throw new WmAPIException(HttpStatus.BAD_REQUEST,"Make sure your order confirmed and dont choose a same team!");
       }
        throw new WmAPIException(HttpStatus.BAD_REQUEST,"OrderId required!");
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

    public Integer getPartimeEmp(Integer teamId,Integer tableNum)
    {
            List<Employees> list= employeeRepository.findAll();
            List<Employees>empList= list.stream().filter(employee -> employee.getTeam_id()==teamId).collect(Collectors.toList());
            Integer partTimeNum=0;
            //defaul team leader
            if(tableNum/4 - empList.size()>0)
            {
                partTimeNum= tableNum/4 -empList.size()+1;
            }
            else{
                partTimeNum=1;
            }
            return partTimeNum;
    }


    public Double getTotalCostofOrder(Orders myOrder,Integer newTbNum)
    {
        Double totalCost=0.0;
         if(myOrder!=null)
        {
           List<FoodDetails> foodtList= myOrder.getFoodDetailsById().stream().toList();
            for (FoodDetails foodDt:foodtList)
            {
                totalCost+=newTbNum*getCostofFood(foodDt.getFoodByFoodId());
            }
            //check null for service
            List<ServiceDetails> servicedtList= myOrder.getServiceDetailsById().stream().toList();
            if(!servicedtList.isEmpty()) {
                for (ServiceDetails svDetail : servicedtList) {
                    totalCost += svDetail.getServicesByServiceId().getCost();
                }
            }
        }


        return totalCost;
    }
    public Double getCostofFood(Food food)
    {
        Double costByFood=0.0;
       List<MaterialDetail> matedtList=food.getMaterialDetailById().stream().toList();
       if(matedtList!=null){
        for (MaterialDetail materialdt:matedtList)
        {
           Integer count= materialdt.getCount();
           Double price= materialdt.getMaterialsByMaterialId().getPrice();
           costByFood += count*price;
        }
       }
       return costByFood;
    }

}

