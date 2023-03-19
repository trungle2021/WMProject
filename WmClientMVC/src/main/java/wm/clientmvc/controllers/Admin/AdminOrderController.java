package wm.clientmvc.controllers.Admin;


import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.*;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;

import java.io.IOException;
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
//@Validated
@RequestMapping("/staff")
public class AdminOrderController {

//RestTemplate restTemplate=new RestTemplate();

    @RequestMapping("/orders/showall")
    public String showAll(Model model, @CookieValue(name = "token",defaultValue = "")String token,@ModelAttribute("alertMessage") String alertMessage)
    {
    model.addAttribute("warningSt", orderStatusWarning);
    model.addAttribute("cancelingSt", orderStatusCancel);
    model.addAttribute("depositedSt", orderStatusDeposited);
    model.addAttribute("orderedSt", orderStatusOrdered);
    model.addAttribute("confirmSt", orderStatusConfirm);
    Authentication authentication=SecurityContextHolder.getContext().getAuthentication();
    String role= authentication.getAuthorities().stream().findFirst().toString();
    ParameterizedTypeReference<List<OrderDTO>> responseType = new ParameterizedTypeReference<>() {
    };
    String url = "http://localhost:8080/api/orders";
    try {
        List<OrderDTO> orderList = APIHelper.makeApiCall(url,
                HttpMethod.GET,
                null,
                token,
                responseType);

        if(role.contains("ADMIN")) {
            model.addAttribute("list", orderList);

        }else if(role.contains("SALE")){
         List<OrderDTO> list=  orderList.stream().filter(order->order.getOrderStatus().equalsIgnoreCase(orderStatusOrdered)).collect(Collectors.toList());
            model.addAttribute("list",list);

        }
        else{
            return "redirect:staff/dashboard";
            }

        if (!alertMessage.isEmpty()) {
            model.addAttribute("alertMessage", alertMessage);
        }
        else {
            model.addAttribute("alertMessage", null);
        }


        return "adminTemplate/pages/tables/order";


    } catch (HttpClientErrorException | IOException ex) {
        model.addAttribute("message", ex.getMessage());
        return "adminTemplate/error";
    }

}

    @RequestMapping("/orders/showmyorder/{status}")

    public String showMyOrder(Model model,@PathVariable String status, @CookieValue(name = "token",defaultValue = "")String token)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails empUserDetails= (CustomUserDetails) authentication.getPrincipal();
        Long empId= empUserDetails.getUserId();
        ParameterizedTypeReference<List<OrderDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        String url="http://localhost:8080/api/orders/bybookingEmp/"+empId;
        try {
            List<OrderDTO>orderList= APIHelper.makeApiCall(url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType);


            List<OrderDTO> myList= orderList.stream().filter(order->order.getOrderStatus().equalsIgnoreCase(status)).collect(Collectors.toList());

            model.addAttribute("list",myList);


            model.addAttribute("warningSt",orderStatusWarning);
            model.addAttribute("cancelingSt",orderStatusCancel);
            model.addAttribute("confirmSt",orderStatusConfirm);
            model.addAttribute("depositedSt",orderStatusDeposited);
            model.addAttribute("orderedSt",orderStatusOrdered);
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
        model.addAttribute("orderDTO",order);
        return "adminTemplate/pages/tables/order-update-status";
    }catch (IOException e) {
        model.addAttribute("message",e.getMessage());
        return "adminTemplate/error";
    }

}

    @RequestMapping("/orders/order-detail-confirm/{id}")
    public String OrderDetailConfirm(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathVariable Integer id)
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
            model.addAttribute("orderDTO",order);
            return "adminTemplate/pages/tables/order-update-confirm";
        }catch (IOException e) {
            model.addAttribute("message",e.getMessage());
            return "adminTemplate/error";
        }

    }

    @RequestMapping(value = "/orders/order-refunded",method = RequestMethod.POST)
    public String OrderRefunded(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathParam("orderId") Integer orderId, @PathParam("status")String status, RedirectAttributes redirectAttributes)
    {
        if(status.equalsIgnoreCase(orderStatusCancel)) {
            String url = "http://localhost:8080/api/orders/updateStatus/"+orderId+"/"+orderStatusRefund;
            try {
                OrderDTO order = APIHelper.makeApiCall(
                        url,
                        HttpMethod.PUT,
                        null,
                        token,
                        OrderDTO.class
                );
                model.addAttribute("orderDTO", order);
                redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Order Refunded! ");
                return "redirect:/staff/orders/showall";
            } catch (IOException e) {
                model.addAttribute("message", e.getMessage());
                return "adminTemplate/error";
            }
        }
        else {
            model.addAttribute("message", "Something wrong! check and contact your leader!");
            return "adminTemplate/error";
        }
        }

        //completed
        @RequestMapping(value = "/orders/order-completed",method = RequestMethod.POST)
        public String OrderCompleted(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathParam("orderId") Integer orderId, @PathParam("status")String status, RedirectAttributes redirectAttributes)
        {
            if(status.equalsIgnoreCase(orderStatusConfirm)) {
                String url = "http://localhost:8080/api/orders/updateStatus/"+orderId+"/"+orderStatusCompleted;
                try {
                    OrderDTO order = APIHelper.makeApiCall(
                            url,
                            HttpMethod.PUT,
                            null,
                            token,
                            OrderDTO.class
                    );
                    model.addAttribute("orderDTO", order);
                    redirectAttributes.addFlashAttribute("alertMessage", "congratulation!Order Completed! ");
                    return "redirect:/staff/orders/showall";
                } catch (IOException e) {
                    model.addAttribute("message", e.getMessage());
                    return "adminTemplate/error";
                }
            }
            else {
                model.addAttribute("message", "Something wrong! check and contact your leader!");
                return "adminTemplate/error";
            }
        }


    @RequestMapping(value = "/orders/order-update",method = RequestMethod.POST)
public String update(@Validated  OrderDTO order, BindingResult bindingResult, Model model, @CookieValue(name="token",defaultValue = "")String token,RedirectAttributes redirectAttributes) {
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

        Integer tbNum=order.getTableAmount();
        Integer min=findOrder.getVenues().getMinPeople() /10;
        Integer max=findOrder.getVenues().getMaxPeople()/10;

            if(tbNum!=null && tbNum<min ||tbNum!=null && tbNum> max )
        {
            bindingResult.rejectValue("tableAmount","tableAmount.range","Please enter table amount from "+min+" to "+ max );

        }
        if (bindingResult.hasErrors()) {
            order.setVenues(findOrder.getVenues());
            order.setCustomersByCustomerId(findOrder.getCustomersByCustomerId());
            model.addAttribute("orderDTO",order);

            return "adminTemplate/pages/tables/order-update-status";

        }
        else{

        String url = "http://localhost:8080/api/orders/updateStatus";
        editOrder.setId(order.getId());
        editOrder.setOrderStatus(orderStatusDeposited);
        editOrder.setBookingEmp(employeeDetails.getUserId().intValue());


        //update total amount if customer didn't set tt table
//        findOrder.setTableAmount(tbNum);
        editOrder.setTableAmount(tbNum);
        editOrder.setOrderTotal(getTotal(findOrder,tbNum));
        Integer team=getTeam(findOrder,token);
        editOrder.setOrganizeTeam(team);
        editOrder.setPartTimeEmpAmount(getPartTimeEmp(team,tbNum,token));



        try {
            APIHelper.makeApiCall(
                    url,
                    HttpMethod.PUT,
                    editOrder,
                    token,
                    OrderDTO.class
            );
            redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Order Deposited! ");
            return "redirect:/staff/orders/showall";
         } catch (IOException e) {
                model.addAttribute("message", e.getMessage());
                return "adminTemplate/error";
            }
        }
    } else {
        model.addAttribute("message", "Oops!Have a Error! Check ordered status or contact your leader!");
        return "adminTemplate/error";
    }

}

@RequestMapping(value = "/orders/order-confirm",method = RequestMethod.POST)
public String updateConfirm(Model model, @CookieValue(name="token",defaultValue = "")String token, @Valid @ModelAttribute OrderDTO order, BindingResult bindingResult,RedirectAttributes redirectAttributes)
{
    //tinh lại total amount and partime emp
    OrderDTO editOrder = new OrderDTO();
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    CustomUserDetails employeeDetails = (CustomUserDetails) authentication.getPrincipal();

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
    if (order.getOrderStatus().equalsIgnoreCase(orderStatusDeposited) && findOrder!=null || order.getOrderStatus().equalsIgnoreCase(orderStatusWarning) && findOrder!=null)
    {
        //check total amount
        Integer tbNum=order.getTableAmount();
        Integer min=findOrder.getVenues().getMinPeople() /10;
        Integer max=findOrder.getVenues().getMaxPeople()/10;
        if(tbNum!=null&&tbNum<min ||tbNum!=null&& tbNum> max)
        {
            bindingResult.rejectValue("tableAmount","tableAmount.range","Please enter table amount form "+min+"to "+ max );

        }
        if (bindingResult.hasErrors()) {
            order.setVenues(findOrder.getVenues());
            order.setCustomersByCustomerId(findOrder.getCustomersByCustomerId());
            order.setOrganizeTeamsByOrganizeTeam(findOrder.getOrganizeTeamsByOrganizeTeam());
            model.addAttribute("orderDTO",order);

            return "adminTemplate/pages/tables/order-update-confirm";

        }
        else {
            // process the order
        String url = "http://localhost:8080/api/orders/updateStatus";
        editOrder.setId(order.getId());
        editOrder.setOrderStatus(orderStatusConfirm);
        editOrder.setBookingEmp(findOrder.getBookingEmp());
        editOrder.setTableAmount(tbNum);
        editOrder.setOrderTotal(getTotal(findOrder,tbNum));
        Integer team=findOrder.getOrganizeTeam();
        editOrder.setOrganizeTeam(team);
        editOrder.setPartTimeEmpAmount(getPartTimeEmp(team,tbNum,token));
            //update
             try {
                APIHelper.makeApiCall(
                    url,
                    HttpMethod.PUT,
                    editOrder,
                    token,
                    OrderDTO.class
                 );
                 redirectAttributes.addFlashAttribute("alertMessage", "congratulation!Order confirm! ");
                    return "redirect:/staff/orders/showall";
                } catch (IOException e) {
                 model.addAttribute("message","Oops! Something Wrong:" + e.getMessage());
                 return "adminTemplate/error";
                }
        }

    } else {
        model.addAttribute("message","Oops! Something Wrong! check your status again!");
        return "adminTemplate/error";
    }
}

//organize team
    @RequestMapping("/orders/showallorder/organize")

    public String showAllbyOrganize(Model model, @CookieValue(name = "token",defaultValue = "")String token)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails empUserDetails= (CustomUserDetails) authentication.getPrincipal();
        Long empId= empUserDetails.getUserId();

        ParameterizedTypeReference<List<OrderDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        //confirm only
        String url = "http://localhost:8080/api/orders/byTeam/empId/"+empId;
        try {
            List<OrderDTO> orderList = APIHelper.makeApiCall(url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType);


                model.addAttribute("list",orderList);

            return "adminTemplate/pages/organize/order-organize-manage";


        } catch (HttpClientErrorException | IOException ex) {
            model.addAttribute("message", ex.getMessage());
            return "adminTemplate/error";
        }

    }






    public Double getTotal(OrderDTO order,Integer tbAmount)
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

            Double total= order.getVenues().getPrice()+servicePrice+foodPrice*tbAmount;
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
                if(!team.getTeamName().equalsIgnoreCase(teamAdmin) && !team.is_deleted()) {
                    int count = 0;
                    for (OrderDTO obj : ordersInMonth) {

                        if (obj.getOrganizeTeam()!=null && team.getId() == obj.getOrganizeTeam()) {
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
            //defaul team leader +2 chef
            if(tableNum/4 -empList.size()>0)
            {partTimeNum= tableNum/4 -empList.size()+3;}
            else{partTimeNum=3;}
            return partTimeNum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}


