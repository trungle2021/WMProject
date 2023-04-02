package wm.clientmvc.controllers.Admin;


import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.*;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;

import java.io.File;
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
    public String showAll(Model model, @CookieValue(name = "token",defaultValue = "")String token,@ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError,HttpServletRequest request,HttpServletResponse response)
    {
        model.addAttribute("warningSt",orderStatusWarning);
        model.addAttribute("cancelingSt",orderStatusCancel);
        model.addAttribute("confirmSt",orderStatusConfirm);
        model.addAttribute("depositedSt",orderStatusDeposited);
        model.addAttribute("orderedSt",orderStatusOrdered);
        model.addAttribute("completedSt",orderStatusCompleted);
        model.addAttribute("refundSt",orderStatusRefund);
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
                responseType,request,response);

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
        if (!alertError.isEmpty()) {
            model.addAttribute("alertError", alertError);
        }
        else {
            model.addAttribute("alertError", null);
        }


        return "adminTemplate/pages/tables/order";


    } catch (HttpClientErrorException | IOException ex) {
        model.addAttribute("message", ex.getMessage());
        return "adminTemplate/error";
    }

}

    @RequestMapping("/orders/showmyorder/{status}")

    public String showMyOrder(Model model,@PathVariable String status, @CookieValue(name = "token",defaultValue = "")String token,HttpServletRequest request,HttpServletResponse response)
    {
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails empUserDetails= (CustomUserDetails) authentication.getPrincipal();
        Long empId= empUserDetails.getUserId();
        model.addAttribute("warningSt",orderStatusWarning);
        model.addAttribute("cancelingSt",orderStatusCancel);
        model.addAttribute("confirmSt",orderStatusConfirm);
        model.addAttribute("depositedSt",orderStatusDeposited);
        model.addAttribute("orderedSt",orderStatusOrdered);
        model.addAttribute("completedSt",orderStatusCompleted);
        model.addAttribute("refundSt",orderStatusRefund);
        ParameterizedTypeReference<List<OrderDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        String url="http://localhost:8080/api/orders/bybookingEmp/"+empId;
        try {
            List<OrderDTO>orderList= APIHelper.makeApiCall(url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType,request,response);

            List<OrderDTO> myList= new ArrayList<>();
            if(orderList!=null)
                {
             myList= orderList.stream().filter(order->order.getOrderStatus().equalsIgnoreCase(status)).collect(Collectors.toList());
                }

            model.addAttribute("list",myList);



            return "adminTemplate/pages/tables/order";
        }
        catch (HttpClientErrorException | IOException ex)
        {
            model.addAttribute("message",ex.getMessage());
            return "adminTemplate/error";
        }
    }




@RequestMapping("/orders/order-detail/{id}")
public String OrderDetail(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathVariable Integer id, HttpServletRequest request, HttpServletResponse response)
{
    String url="http://localhost:8080/api/orders/"+id;
    try {
        OrderDTO order= APIHelper.makeApiCall(
                url,
                HttpMethod.GET,
                null,
                token,
                OrderDTO.class,request,response
        );
        model.addAttribute("orderDTO",order);
        return "adminTemplate/pages/tables/order-update-status";
    }catch (IOException e) {
        model.addAttribute("message",e.getMessage());
        return "adminTemplate/error";
    }

}

    @RequestMapping("/orders/order-detail-confirm/{id}")
    public String OrderDetailConfirm(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathVariable Integer id,HttpServletRequest request, HttpServletResponse response)
    {
        String url="http://localhost:8080/api/orders/"+id;
        try {
            OrderDTO order= APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    OrderDTO.class,request,response
            );
            model.addAttribute("orderDTO",order);
            return "adminTemplate/pages/tables/order-update-confirm";
        }catch (Exception e) {
            model.addAttribute("message",e.getMessage());
            return "adminTemplate/error";
        }

    }

         @RequestMapping(value = "/orders/order-refunded",method = RequestMethod.POST)
             public String OrderRefunded(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathParam("orderId") Integer orderId, @PathParam("status")String status, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response)
             {
        if(status.equalsIgnoreCase(orderStatusCancel)) {
            String url = "http://localhost:8080/api/orders/updateStatus/"+orderId+"/"+orderStatusRefund;
            try {
                OrderDTO order = APIHelper.makeApiCall(
                        url,
                        HttpMethod.PUT,
                        null,
                        token,
                        OrderDTO.class,request,response
                );
                model.addAttribute("orderDTO", order);
                redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Order Refunded! ");
                return "redirect:/staff/orders/showall";
            } catch (Exception e) {
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
        public String OrderCompleted(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathParam("orderId") Integer orderId, @PathParam("status")String status, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response) {

            if (status.equalsIgnoreCase(orderStatusConfirm)) {
                OrderDTO editOrder = new OrderDTO();
                String orderUrl = "http://localhost:8080/api/orders/" + orderId;
                OrderDTO findOrder = new OrderDTO();
                try {
                    findOrder = APIHelper.makeApiCall(
                            orderUrl,
                            HttpMethod.GET,
                            null,
                            token,
                            OrderDTO.class,request,response
                    );

                } catch (Exception e) {
                    model.addAttribute("message", e.getMessage());
                    return "adminTemplate/error";
                }
                //check ngày
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime eventTime= LocalDateTime.parse(findOrder.getTimeHappen(),formatter);
                LocalDateTime now =  LocalDateTime.now();
                if(now.isBefore(eventTime))
                {
                    redirectAttributes.addFlashAttribute("alertError", "Oops! You Can't Complete order before event day! ");
                    return "redirect:/staff/teams/showallorder/organize";
                }


                //tính lại total.ko thay team,ko thay partime

                if (findOrder != null) {
                    String url = "http://localhost:8080/api/orders/updateStatus";
                    editOrder.setId(orderId);
                    editOrder.setOrderStatus(orderStatusCompleted);
                    editOrder.setBookingEmp(findOrder.getBookingEmp());

                    Integer tbNum = findOrder.getTableAmount();
                    editOrder.setTableAmount(tbNum);

                    // update total when conpleted
                    editOrder.setOrderTotal(getTotal(findOrder, tbNum));
                    editOrder.setContract(findOrder.getContract());
                    //no render team
                    editOrder.setOrganizeTeam(findOrder.getOrganizeTeam());
                    editOrder.setPartTimeEmpAmount(findOrder.getPartTimeEmpAmount());

                    //update
                    try {
                        APIHelper.makeApiCall(
                                url,
                                HttpMethod.PUT,
                                editOrder,
                                token,
                                OrderDTO.class,request,response
                        );

                        redirectAttributes.addFlashAttribute("alertMessage", "congratulation!Party Completed!Nice Job Team! ");
                        return "redirect:/staff/teams/showallorder/organize";
                    }catch (Exception e)
                    {
                        model.addAttribute("message", "Something wrong! check and contact your leader!");
                        return "adminTemplate/error";
                    }

                }
                else {
                    model.addAttribute("message", "Something wrong! check and contact your leader!");
                    return "adminTemplate/error";
                }
            }
            else {
                model.addAttribute("message", "Something wrong! check and contact your leader!");
                return "adminTemplate/error";
            }
        }


    @RequestMapping(value = "/orders/order-update",method = RequestMethod.POST)
public String update(@Validated  OrderDTO order, BindingResult bindingResult, Model model, @CookieValue(name="token",defaultValue = "")String token,
                     RedirectAttributes redirectAttributes, @PathParam("file") MultipartFile file,HttpServletRequest request, HttpServletResponse response) throws IOException {


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
                OrderDTO.class,request,response
        );
    } catch (Exception e) {
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
            try {
            ClientUtilFunction utilFunction = new ClientUtilFunction();
            String contract = utilFunction.AddFileEncrypted(file);
        String url = "http://localhost:8080/api/orders/updateStatus";
        editOrder.setId(order.getId());
        editOrder.setOrderStatus(orderStatusDeposited);
        editOrder.setBookingEmp(employeeDetails.getUserId().intValue());
        editOrder.setContract(contract);


        //update total amount if customer didn't set tt table
//        findOrder.setTableAmount(tbNum);
        editOrder.setTableAmount(tbNum);
        editOrder.setOrderTotal(getTotal(findOrder,tbNum));

//        Integer team=getTeam(findOrder,token);
//        editOrder.setOrganizeTeam(team);
//
//        editOrder.setPartTimeEmpAmount(getPartTimeEmp(team,tbNum,token));


            APIHelper.makeApiCall(
                    url,
                    HttpMethod.PUT,
                    editOrder,
                    token,
                    OrderDTO.class,request,response
            );
            redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Order Deposited! ");
            return "redirect:/staff/orders/showall";
         } catch (Exception e) {
                model.addAttribute("message","fail!"+ e.getMessage());
                return "adminTemplate/error";
            }
        }
    } else {
        model.addAttribute("message", "Oops!Have a Error! Check ordered status or contact your leader!");
        return "adminTemplate/error";
    }

}

@RequestMapping(value = "/orders/order-confirm",method = RequestMethod.POST)
public String updateConfirm(Model model, @CookieValue(name="token",defaultValue = "")String token, @Valid @ModelAttribute OrderDTO order, BindingResult bindingResult,RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response)
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
                OrderDTO.class,request,response
        );
    } catch (IOException e) {
        model.addAttribute("message", e.getMessage());
        return "adminTemplate/error";
    }
    if (order.getOrderStatus().equalsIgnoreCase(orderStatusDeposited) && findOrder!=null || order.getOrderStatus().equalsIgnoreCase(orderStatusWarning) && findOrder!=null)
    {

        if(findOrder.getFoodDetailsById()==null)
        {
            redirectAttributes.addFlashAttribute("alertError", "Oops Make Sure That The Menu was selected!Contact Customer ");
            return "redirect:/staff/orders/showall";
        }
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
        //no update total when confirm
        editOrder.setOrderTotal(findOrder.getOrderTotal());

        editOrder.setContract(findOrder.getContract());
        //render team
        Integer team=getTeam(findOrder,token,request,response);
        editOrder.setOrganizeTeam(team);
        editOrder.setPartTimeEmpAmount(getPartTimeEmp(team,tbNum,token,request,response));
            //update
             try {
                APIHelper.makeApiCall(
                    url,
                    HttpMethod.PUT,
                    editOrder,
                    token,
                    OrderDTO.class,request,response
                 );
                 redirectAttributes.addFlashAttribute("alertMessage", "congratulation!Order confirm! ");
                    return "redirect:/staff/orders/showall";
                } catch (Exception e) {
                 model.addAttribute("message","Oops! Something Wrong:" + e.getMessage());
                 return "adminTemplate/error";
                }
        }

    } else {
        model.addAttribute("message","Oops! Something Wrong! check your status again!");
        return "adminTemplate/error";
    }
}


    @RequestMapping(value = "orders/order-uncompleted",method = RequestMethod.POST)
    public String OrderUncompleted(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathParam("orderId") Integer orderId, @PathParam("status")String status, RedirectAttributes redirectAttributes,HttpServletRequest request, HttpServletResponse response)
    {

        if(status.equalsIgnoreCase(orderStatusConfirm)) {

            String orderUrl = "http://localhost:8080/api/orders/" + orderId;
            OrderDTO findOrder = new OrderDTO();
            try {
                findOrder = APIHelper.makeApiCall(
                        orderUrl,
                        HttpMethod.GET,
                        null,
                        token,
                        OrderDTO.class,request,response
                );

            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "adminTemplate/error";
            }
            //check ngày
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime eventTime= LocalDateTime.parse(findOrder.getTimeHappen(),formatter);
            LocalDateTime now =  LocalDateTime.now();
            if(now.isBefore(eventTime))
            {
                redirectAttributes.addFlashAttribute("alertError", "Oops! You Can't Mark Uncomplete order before event day! ");
                return "redirect:/staff/orders/showall";
            }


            String url = "http://localhost:8080/api/orders/updateStatus/"+orderId+"/"+orderStatusUncompleted;
            try {
                OrderDTO order = APIHelper.makeApiCall(
                        url,
                        HttpMethod.PUT,
                        null,
                        token,
                        OrderDTO.class,request,response
                );
                model.addAttribute("orderDTO", order);
                redirectAttributes.addFlashAttribute("alertMessage", "Mark as Uncompleted!");
                return "redirect:/staff/orders/showall";
            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "adminTemplate/error";
            }
        }
        else {
            model.addAttribute("message", "Something wrong! check and contact your leader!");
            return "adminTemplate/error";
        }
    }



    public Double getTotal(OrderDTO order,Integer tbAmount)
    {
        Double foodPrice=0.0;
        Double servicePrice = 0.0;
        if(order.getFoodDetailsById()!=null){

            for (FoodDetailDTO food:order.getFoodDetailsById()) {
                foodPrice += food.getFoodByFoodId().getPrice();
            }
        }
        if(order.getServiceDetailsById()!=null) {
            for (ServiceDetailDTO servicedt : order.getServiceDetailsById()) {
                servicePrice += servicedt.getServicesByServiceId().getPrice();
            }
        }
            Double total= order.getVenues().getPrice()+servicePrice+foodPrice*tbAmount;
            return total;
    }

    public Integer getTeam(OrderDTO order,String token,HttpServletRequest request,HttpServletResponse response)
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
                    reponseOrder,request,response
            );

            List<OrganizeTeamDTO> teamList= APIHelper.makeApiCall(
                    teamUrl,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTeam,request,response
            );

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime timeHappen =  LocalDateTime.parse(order.getTimeHappen(),formatter);
        Month happenMonth=timeHappen.getMonth();
        //get order in month happen

            //fix
        List<OrderDTO> ordersInMonth= new ArrayList<>();
            for (OrderDTO od:orderList)
            {
             if(
                     //same month and status in(completed,uncompleted,confirm)
                     LocalDateTime.parse(od.getTimeHappen(),formatter).getMonth().compareTo(happenMonth)==0 && od.getOrderStatus().equals(orderStatusConfirm)
                     ||  LocalDateTime.parse(od.getTimeHappen(),formatter).getMonth().compareTo(happenMonth)==0 && od.getOrderStatus().equals(orderStatusCompleted)
                     ||  LocalDateTime.parse(od.getTimeHappen(),formatter).getMonth().compareTo(happenMonth)==0 && od.getOrderStatus().equals(orderStatusUncompleted)
             )
             {ordersInMonth.add(od);}
            }
    Map<Integer,Integer> map=new HashMap<>();
            for (OrganizeTeamDTO team:teamList)
            {
                if(!team.getTeamName().equalsIgnoreCase(teamAdmin) && !team.is_deleted() && team.getTeamsize()!=0) {
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

    public Integer getPartTimeEmp(Integer teamId,Integer tableNum,String token,HttpServletRequest request,HttpServletResponse response)
    {
        String url="http://localhost:8080/api/employees/findByTeam/"+teamId;
ParameterizedTypeReference<List<EmployeeDTO>> responseType = new ParameterizedTypeReference<List<EmployeeDTO>>() {};
        try {
            List<EmployeeDTO> empList =APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType,request,response
            );
            Integer partTimeNum=0;
            //defaul team leader 
            if(tableNum/4 -empList.size()>0)
            {partTimeNum= tableNum/4 -empList.size()+1;}
            else{partTimeNum=1;}
            return partTimeNum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}


