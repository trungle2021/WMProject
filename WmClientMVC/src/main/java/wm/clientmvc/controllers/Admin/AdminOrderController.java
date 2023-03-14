package wm.clientmvc.controllers.Admin;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import wm.clientmvc.DTO.*;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.Static_Status;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static wm.clientmvc.utils.Static_Status.*;

@Controller
@RequestMapping("/staff")
public class AdminOrderController {

RestTemplate restTemplate=new RestTemplate();

@RequestMapping("/orders/showall")

public String showAll(Model model, @CookieValue(name = "token",defaultValue = "")String token)
{
    ParameterizedTypeReference<List<OrderDTO>> responseType = new ParameterizedTypeReference<List<OrderDTO>>() {};
    String url="http://localhost:8080/api/orders";
    try {
       List<OrderDTO>orderList= APIHelper.makeApiCall(url,
                HttpMethod.GET,
                null,
                token,
                responseType);

       model.addAttribute("list",orderList);


        return "adminTemplate/pages/tables/order";
    }
    catch (HttpClientErrorException | IOException ex)
    {
        model.addAttribute("message",ex.getMessage());
        return "adminTemplate/error";
    }
}
@RequestMapping("/orders/order-detail/{id}")
public String OrderDetail(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathVariable Integer id)
{
    String url="http://localhost:8080/api/orders/"+id;
    try {
        OrderDTO order= APIHelper.makeApiCall(
                url,
                HttpMethod.GET,
                null,
                token,
                OrderDTO.class
        );
        model.addAttribute("order",order);
        return "adminTemplate/pages/tables/order-update-status";
    }catch (IOException e) {
        model.addAttribute("message",e.getMessage());
        return "adminTemplate/error";
    }

}
@RequestMapping(value = "/orders/order-update",method = RequestMethod.POST)
public String update(Model model, @CookieValue(name="token",defaultValue = "")String token, @ModelAttribute OrderDTO order) {
    OrderDTO editOrder = new OrderDTO();
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails employeeDetails = (CustomUserDetails) authentication.getPrincipal();

    String orderUrl = "http://localhost:8080/api/orders/" + order.getId();
    OrderDTO findOrder= new OrderDTO();
    try {
        findOrder = APIHelper.makeApiCall(
                orderUrl,
                HttpMethod.GET,
                null,
                token,
                OrderDTO.class
        );
    } catch (IOException e) {
        model.addAttribute("message", e.getMessage());
        return "adminTemplate/error";
    }
    if (order.getOrderStatus().equalsIgnoreCase(orderStatusOrdered) && findOrder!=null) {

        //call API order


        //
        String url = "http://localhost:8080/api/orders/updateStatus";
        editOrder.setId(order.getId());
        editOrder.setOrderStatus(orderStatusDeposited);
        editOrder.setBookingEmp(employeeDetails.getUserId().intValue());
        editOrder.setOrderTotal(getOrderTotal(findOrder));
        Integer team=getTeam(findOrder,token);
        editOrder.setOrganizeTeam(team);
        Integer tbNum=0;
        if(order.getTableAmount()==null)
        {tbNum=order.getVenues().getMinPeople()/10;}
        else{tbNum=order.getTableAmount();}
        editOrder.setPartTimeEmpAmount(getPartTimeEmp(team,tbNum,token));



        try {
            APIHelper.makeApiCall(
                    url,
                    HttpMethod.PUT,
                    editOrder,
                    token,
                    OrderDTO.class
            );

            return "redirect:/staff/orders/showall";
        } catch (IOException e) {
            model.addAttribute("message", e.getMessage());
            return "adminTemplate/error";
        }
    } else {
        model.addAttribute("message", "Kiểm Tra lại Tình Trạng Đơn! Có Lỗi Xảy ra!");
        return "adminTemplate/error";
    }

}

        @RequestMapping("/test")
    public String test(){return "adminTemplate/home";}


    public Double getOrderTotal(OrderDTO order)
    {
            Double foodPrice=0.0;
            for (FoodDetailDTO food:order.getFoodDetailsById()) {
                foodPrice += food.getFoodByFoodId().getPrice();
            }
            Double servicePrice=0.0;
            for (ServiceDetailDTO servicedt:order.getServiceDetailsById())
            {
                servicePrice+=servicedt.getServicesByServiceId().getPrice();
            }
            Integer tableAmount =order.getTableAmount();
            Double total= order.getVenues().getPrice()+servicePrice+foodPrice*tableAmount;
            return total;
    }

    public Integer getTeam(OrderDTO order,String token)
    {
        Integer teamId;
        String myTimeHappend=order.getTimeHappen();
        Integer myVenueId=order.getVenueId();
        ParameterizedTypeReference<List<OrderDTO>> reponseOrder=new ParameterizedTypeReference<List<OrderDTO>>() {};
        ParameterizedTypeReference<List<OrganizeTeamDTO>> responseTeam = new ParameterizedTypeReference<List<OrganizeTeamDTO>>() {};

        String orderlistUrl= "http://localhost:8080/api/orders";

        String teamUrl="http://localhost:8080/api/teams/all";
        try {
            List<OrderDTO> orderList=APIHelper.makeApiCall(
                    orderlistUrl,
                    HttpMethod.GET,
                    null,
                    token,
                    reponseOrder
            );

            List<OrganizeTeamDTO> teamList= APIHelper.makeApiCall(
                    teamUrl,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTeam
            );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeHappen =  LocalDateTime.parse(order.getTimeHappen(),formatter);
        Month happenMonth=timeHappen.getMonth();
        //get order in month happen
        List<OrderDTO> ordersInMonth= new ArrayList<>();
            for (OrderDTO od:orderList)
            {
             if(LocalDateTime.parse(od.getTimeHappen(),formatter).getMonth().compareTo(happenMonth)==0 && od.getOrderStatus().equals(orderStatusDeposited)||
                     LocalDateTime.parse(od.getTimeHappen(),formatter).getMonth().compareTo(happenMonth)==0 && od.getOrderStatus().equals(orderStatusConfirm)
             )
             {ordersInMonth.add(od);}
            }
    Map<Integer,Integer> map=new HashMap<>();
            for (OrganizeTeamDTO team:teamList)
            {
                if(!team.getTeamName().equalsIgnoreCase(teamAdmin)) {
                    int count = 0;
                    for (OrderDTO obj : ordersInMonth) {

                        if (team.getId() == obj.getOrganizeTeam()) {
                            count += 1;
                        }
                    }
                    map.put(team.getId(), count);

                }
            }

            //lấy giá trị lớn nhất và lớn nhì
            Map.Entry<Integer, Integer> minEntry = null;
            Map.Entry<Integer, Integer> secondMinEntry = null;
            for (Map.Entry<Integer, Integer> entry : map.entrySet()) {
                if (minEntry == null || entry.getValue().compareTo(minEntry.getValue()) < 0) {
                    secondMinEntry = minEntry;
                    minEntry = entry;
                } else if ((secondMinEntry == null || entry.getValue().compareTo(secondMinEntry.getValue()) < 0)
                        && entry != minEntry) {
                    secondMinEntry = entry;
                }
            }
//            LocalDateTime orderHappend =LocalDateTime.parse(myTimeHappend,formatter);
            //filter orderList happend in orderHappend
            List<OrderDTO> orderHappendList=new ArrayList<>();
            for (OrderDTO o:ordersInMonth) {
                if(o.getTimeHappen().equals(myTimeHappend))
                {
                    orderHappendList.add(o);
                }

            }
            teamId= minEntry.getKey();
            for (OrderDTO obj:orderHappendList)
            {
             if(obj.getOrganizeTeam() == minEntry.getKey())
             {
                 teamId= secondMinEntry.getKey();
                 break;
             }

            }




        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return teamId;
    }

    public Integer getPartTimeEmp(Integer teamId,Integer tableNum,String token)
    {
        String url="http://localhost:8080/api/employees/findByTeam/"+teamId;
ParameterizedTypeReference<List<EmployeeDTO>> responseType = new ParameterizedTypeReference<List<EmployeeDTO>>() {};
        try {
            List<EmployeeDTO> empList =APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType
            );
            Integer partTimeNum=0;
            if(tableNum/4 -empList.size()>0)
            {partTimeNum= tableNum/4 -empList.size();}
            else{partTimeNum=0;}
            return partTimeNum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }




}


