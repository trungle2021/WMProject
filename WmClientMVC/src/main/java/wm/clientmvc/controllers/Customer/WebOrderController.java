package wm.clientmvc.controllers.Customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

//import com.google.gson.Gson;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.*;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;

import static wm.clientmvc.utils.Static_Status.*;

@Controller
@RequestMapping("/customers/orders")
public class WebOrderController {

    RestTemplate restTemplate = new RestTemplate();
    String url = "http://localhost:8080/api/venues/allactive";
    String orderurl="http://localhost:8080/api/orders";

        @GetMapping("")
        public String order(){
            return "orderpage";
        }

        @RequestMapping("/getvenue")
        @ResponseBody
        public ResponseEntity<String> showVenue(@RequestBody String date,@CookieValue(name="token",defaultValue = "") String token) throws JsonProcessingException {
            Map<String, Object> map = new HashMap<>();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);
        try{
            HttpEntity<?> entity = new HttpEntity<>(headers);
            ResponseEntity<List<VenueDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<VenueDTO>>() {}
            );

            List<VenueDTO> venueList = response.getBody();
  //get active venueList

//
            ResponseEntity<List<OrderDTO>> orderResponse = restTemplate.exchange(
                    orderurl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<OrderDTO>>() {}
            );
            List<OrderDTO> orderList = orderResponse.getBody();

//        List find in date
            List<OrderDTO> bookedList=new ArrayList<>();


            if(orderList!=null){
            for (OrderDTO order: orderList) {
                if (order.getTimeHappen().contains(date) && !order.getOrderStatus().equals(orderStatusCanceled)&& !order.getOrderStatus().equals(orderStatusCancel)&& !order.getOrderStatus().equals(orderStatusRefund)) {
                    bookedList.add(order);
                }
            }
            }
//        check venue available
            //fiter venue
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

            List<VenueBooked> bookeds=new ArrayList<>();
            if(bookedList!=null){
//                int i=1;
            for (OrderDTO order:bookedList)
            {
                LocalTime timeHappen = LocalDateTime.parse(order.getTimeHappen(), formatter).toLocalTime();
                LocalTime compareTime1 = LocalTime.parse("13:00:00");
                LocalTime compareTime2 = LocalTime.parse("18:00:00");
                if(timeHappen.isBefore(compareTime1)){
                VenueBooked newbooked=new VenueBooked();
//                newbooked.setBookedDay(date);
                newbooked.setVenueId(String.valueOf(order.getVenueId()));
                newbooked.setBookedTime("Afternoon");
                bookeds.add(newbooked);
//                i++;
                }
                else if(timeHappen.isAfter(compareTime1) && timeHappen.isBefore(compareTime2)){
                    VenueBooked newbooked=new VenueBooked();
//                    newbooked.setBookedDay(date);
                    newbooked.setVenueId(String.valueOf(order.getVenueId()));
                    newbooked.setBookedTime("Evening");
                    bookeds.add(newbooked);
//                    i++;
                }
             }
            }

            String json = toJson(venueList,bookeds);

            return ResponseEntity.ok(json);}
        catch (Exception ex)
            {
                return new ResponseEntity<>(ex.getMessage(),HttpStatus.BAD_REQUEST);
            }

    }

    @RequestMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createOrder(@RequestBody String jsonData,@CookieValue(name="token",defaultValue = "") String token) throws JsonProcessingException {

        DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ObjectMapper objectMapper = new ObjectMapper();
        //get JSON from ajax
        Map<String, String> data = objectMapper.readValue(jsonData, new TypeReference<Map<String,String>>(){});

        Integer venueId=Integer.parseInt(data.get("venueId"));
        String dateTime=new String();
        if(data.get("bookType").equalsIgnoreCase("Afternoon"))
        {
             dateTime=data.get("day")+" 12:00:00";
        }
        else if(data.get("bookType").equalsIgnoreCase("Evening"))
        {
             dateTime=data.get("day")+" 17:00:00";
        }
        else{ return new ResponseEntity<>("Oops!Some Thing Wrong!",HttpStatus.BAD_REQUEST);}
        LocalDateTime orderDateTime = LocalDateTime.now();
        String formattedNow = orderDateTime.format(formatter);
        LocalDateTime happenDateTime = LocalDateTime.parse(dateTime, formatter);

//        LocalTime timeHappen = happenDateTime.toLocalTime();
//check status
        Duration duration=Duration.between(orderDateTime,happenDateTime);
        if(duration.toDays() >=30 && happenDateTime.isAfter(orderDateTime)) {
//            HttpHeaders httpHeaders = new HttpHeaders();
//            httpHeaders.setBearerAuth(token);

            OrderDTO newOrder = new OrderDTO();
            newOrder.setVenueId(venueId);
            newOrder.setTimeHappen(dateTime);
            //set cung test
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            CustomUserDetails customerDetail =(CustomUserDetails) authentication.getPrincipal();

            newOrder.setCustomerId(customerDetail.getUserId().intValue());
            //
            newOrder.setOrderStatus(orderStatusOrdered);
            newOrder.setOrderDate(formattedNow);
//            HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(newOrder, httpHeaders);
            try {
                OrderDTO responseOrder= APIHelper.makeApiCall(
                        "http://localhost:8080/api/orders/create",
                        HttpMethod.POST,
                        newOrder,
                        token,
                        OrderDTO.class
                );
                if(responseOrder!=null)
                {
                return ResponseEntity.ok(objectMapper.writeValueAsString(responseOrder));
                }

                else{
                    return new ResponseEntity<>("Oops! Some thing wrong! This venues already booked!",HttpStatus.BAD_REQUEST);
                }
            } catch (IOException e) {
                return new ResponseEntity<>("Oops! Some thing wrong!Try again",HttpStatus.BAD_REQUEST);
            }
//            ResponseEntity<OrderDTO> responseEntity = restTemplate.postForEntity("http://localhost:8080/api/orders/create", requestEntity, OrderDTO.class);

// Get the response body
//            OrderDTO responseBody = responseEntity.getBody();

        }
        else{

            return new ResponseEntity<>("You need to book 30 days in advance!",HttpStatus.BAD_REQUEST);

        }
    }


    @RequestMapping(value="/create-detail",method = RequestMethod.POST)
    public String createDetail(Model model, @RequestParam("orderId") int orderId ,@CookieValue(name="token",defaultValue = "") String token) {
        String orderUrl="http://localhost:8080/api/orders/"+orderId;
//        model.addAttribute("orderId",orderId);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
//getorder

        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                orderUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<OrderDTO>() {}

        );
        OrderDTO myOrder=response.getBody();
        //get foodlist
       String foodUrl="http://localhost:8080/api/food/allactive";
        ResponseEntity<List<FoodDTO>> foodResponse = restTemplate.exchange(
                foodUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<FoodDTO>>() {}

        );
        List<FoodDTO> foodList= foodResponse.getBody();
        //get serviceList
        String serviceUrl="http://localhost:8080/api/services/allactive";
        ResponseEntity<List<ServiceDTO>> serviceResponse = restTemplate.exchange(
                serviceUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ServiceDTO>>() {}

        );
        List<ServiceDTO> serviceList= serviceResponse.getBody();
        model.addAttribute("myOrder",myOrder);

        model.addAttribute("foodList",foodList);
        model.addAttribute("serviceList",serviceList);


        return "neworder";
    }
    @RequestMapping(value="/create-order", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createNewOrder(@RequestBody String jsonData,@CookieValue(name="token",defaultValue = "") String token) throws IOException {
        // Process the request data here...

        ObjectMapper objectMapper = new ObjectMapper();
        //get JSON from ajax
        Map<String, Object> data = objectMapper.readValue(jsonData, new TypeReference<Map<String,Object>>(){});
        // Return a response indicating success or failure
        Integer orderId=Integer.parseInt(data.get("orderId").toString());
        List<String> foodData=objectMapper.readValue(objectMapper.writeValueAsString(data.get("foodList")), new TypeReference< List<String>>() {});
        List<String> svData=objectMapper.readValue(objectMapper.writeValueAsString(data.get("serviceList")), new TypeReference<List<String>>() {});
        Integer tableAmount= Integer.parseInt(data.get("table").toString());
        if(tableAmount<=0)
        {
            return new ResponseEntity<>("Choose the number of table!",HttpStatus.BAD_REQUEST);
        }

        String createFDUrl="http://localhost:8080/api/foodDetails/create";
        String createSDUrl="http://localhost:8080/api/servicedetails/create";
        String updateTableUrl="http://localhost:8080/api/orders/updateTable/"+orderId+"/"+tableAmount;
//        model.addAttribute("orderId",orderId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);

        for (String foodId:foodData) {
            FoodDetailDTO newFoodDetail= new FoodDetailDTO();
            newFoodDetail.setFoodId(Integer.parseInt(foodId));
            newFoodDetail.setOrderId(orderId);
            HttpEntity<?> entity=new HttpEntity<>(newFoodDetail,headers);

                    restTemplate.postForEntity(
                    createFDUrl,
                    entity,
                    FoodDetailDTO.class);

        }

        for (String svId:svData) {
            ServiceDetailDTO newSVDetail= new ServiceDetailDTO();
            newSVDetail.setServiceId(Integer.parseInt(svId));
            newSVDetail.setOrderId(orderId);
            HttpEntity<?> entity=new HttpEntity<>(newSVDetail,headers);

                    restTemplate.postForEntity(
                    createSDUrl,
                    entity,
                    FoodDetailDTO.class);

        }

                APIHelper.makeApiCall(
                updateTableUrl,
                HttpMethod.PUT,
                null,
                token,
                OrderDTO.class
        );

//getorder

        return ResponseEntity.ok("{\"message\": \"Congratulations on selecting a successful dish and service!\"}");
    }

    //CANCELING
    @RequestMapping(value = "/myorder/order-cancel",method = RequestMethod.POST)
    public String OrderCancel(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathParam("orderId") Integer orderId, @PathParam("status")String status,@PathParam("confirmMess")String confirmMess, RedirectAttributes redirectAttributes)
    {

        if(confirmMess==null || !confirmMess.equalsIgnoreCase(confirmCancel))
        {
            redirectAttributes.addFlashAttribute("alertError", "Can't Cancel booking!Your confirm message incorrect!Try again");
            //redirect
            return "redirect:/customers/dashboard";
        }


        if(status.equalsIgnoreCase(orderStatusDeposited)|| status.equalsIgnoreCase(orderStatusWarning))
        {
            String url = "http://localhost:8080/api/orders/updateStatus/"+orderId+"/"+orderStatusCancel;
            try {
               APIHelper.makeApiCall(
                        url,
                        HttpMethod.PUT,
                        null,
                        token,
                        OrderDTO.class
                );
//                model.addAttribute("orderDTO", order);
                redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Your order canceling,wait for our employee accept!Or contact us to get refund!");
                //return customer profile
                return "redirect:/customers/dashboard";
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("alertError", "Oops! Something wrong!Server poor connection!Check Your connection and try again!");
                //redirect
                return "redirect:/customers/dashboard";
            }
        }
        else {
            redirectAttributes.addFlashAttribute("alertError", "Oops! Something wrong!Make sure your order status is deposited or warning!");
            return "redirect:/customers/dashboard";
        }
    }
//confirm order
    @RequestMapping(value = "/myorder/order-confirm",method = RequestMethod.POST)
    public String OrderConfirm(Model model, @CookieValue(name="token",defaultValue = "")String token, @PathParam("orderId") Integer orderId, RedirectAttributes redirectAttributes)
    {
        String url="http://localhost:8080/api/orders/"+orderId;
        try {
            OrderDTO order= APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    OrderDTO.class
            );
            model.addAttribute("orderDTO",order);
            return "customerTemplate/customer-order-confirm";
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertError", "Oops! Something wrong!Server poor connection!Check Your connection and try again!");
            //redirect
            return "redirect:/customers/dashboard";
        }

    }
    //update confirm
    @RequestMapping(value = "/myorder/update-confirm",method = RequestMethod.POST)
    public String updateConfirm(Model model, @CookieValue(name="token",defaultValue = "")String token, @Valid @ModelAttribute OrderDTO order, BindingResult bindingResult, RedirectAttributes redirectAttributes)
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
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertError", "Oops! Something wrong!Server poor connection!Check Your connection and try again!");
            //redirect
            return "redirect:/customers/dashboard";
        }
        if (order.getOrderStatus().equalsIgnoreCase(orderStatusDeposited) && findOrder!=null || order.getOrderStatus().equalsIgnoreCase(orderStatusWarning) && findOrder!=null)
        {

            if(findOrder.getFoodDetailsById()==null)
            {
                redirectAttributes.addFlashAttribute("alertError", "Oops Make Sure That Your Food Menu have been chosen! ");
                return "redirect:/customers/dashboard";
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

                return "customerTemplate/customer-order-confirm";

            }
            else {
                // process the order
                String url = "http://localhost:8080/api/orders/updateStatus";
                editOrder.setId(order.getId());
                editOrder.setOrderStatus(orderStatusConfirm);
                editOrder.setBookingEmp(findOrder.getBookingEmp());
                editOrder.setTableAmount(tbNum);
                //no update amount when confirm
                editOrder.setOrderTotal(findOrder.getOrderTotal());
                editOrder.setContract(findOrder.getContract());
                //get team
                Integer team=getTeam(findOrder,token);
                editOrder.setOrganizeTeam(team);
                //render number of partime
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
                    redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Order confirm! ");

                    return "redirect:/customers/dashboard";
                } catch (IOException e) {
                    redirectAttributes.addFlashAttribute("alertError", "Oops! Something wrong!Server poor connection!Check Your connection and try again!");
                    //redirect
                    return "redirect:/customers/dashboard";
                }
            }

        } else {
            redirectAttributes.addFlashAttribute("alertError", "Oops! Something wrong!Server poor connection!Check Your connection and try again!");
            //redirect
            return "redirect:/customers/dashboard";
        }
    }

    //orderdetail
    @RequestMapping(value = "/myorder/order-detail",method = RequestMethod.POST)
    public String OrderDetail(Model model,@CookieValue(name="token",defaultValue = "")String token,@PathParam("orderId") Integer orderId,RedirectAttributes redirectAttributes)
    {
        model.addAttribute("warningSt",orderStatusWarning);
        model.addAttribute("cancelingSt",orderStatusCancel);
        model.addAttribute("confirmSt",orderStatusConfirm);
        model.addAttribute("depositedSt",orderStatusDeposited);
        model.addAttribute("orderedSt",orderStatusOrdered);
        model.addAttribute("confirmCancel",confirmCancel);

        String url="http://localhost:8080/api/orders/"+orderId;
        try {
            OrderDTO order=APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    OrderDTO.class
            );
//            DateTimeFormatter formatter= DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");


//            String orderDay = LocalDateTime.parse(order.getOrderDate(), formatter).toString();
//            String eventDay=LocalDateTime.parse(order.getTimeHappen(),formatter).toString();
            model.addAttribute("myOrder",order);
//            String
            return "customerTemplate/order-detail";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertError", "Oops! Something wrong!Server poor connection!Check Your connection and try again!");
            //redirect
            return "redirect:/customers/dashboard";
        }


//        return "customerTemplate/order-detail";
    }

    //order detail update page
    @RequestMapping(value="/myorder/order-detail/update",method = RequestMethod.POST)
    public String updateDetail(Model model, @RequestParam("orderId") int orderId ,@CookieValue(name="token",defaultValue = "") String token,RedirectAttributes redirectAttributes) {
        String orderUrl="http://localhost:8080/api/orders/"+orderId;
//        model.addAttribute("orderId",orderId);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(headers);
//getorder
        OrderDTO myOrder= new OrderDTO();
        try {
    ResponseEntity<OrderDTO> response = restTemplate.exchange(
            orderUrl,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<OrderDTO>() {
            }

    );
        myOrder=response.getBody();

        List<FoodDTO> foodList= new ArrayList<>();
        //get foodlist

            String foodUrl = "http://localhost:8080/api/food/allactive";
            ResponseEntity<List<FoodDTO>> foodResponse = restTemplate.exchange(
                    foodUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<FoodDTO>>() {
                    }

            );
             foodList = foodResponse.getBody();

        //get serviceList
        List<ServiceDTO> serviceList= new ArrayList<>();

            String serviceUrl = "http://localhost:8080/api/services/allactive";
            ResponseEntity<List<ServiceDTO>> serviceResponse = restTemplate.exchange(
                    serviceUrl,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<ServiceDTO>>() {
                    }

            );
           serviceList = serviceResponse.getBody();

//        model.addAttribute("alertError", null);

        model.addAttribute("myOrder",myOrder);
        model.addAttribute("foodList",foodList);
        model.addAttribute("serviceList",serviceList);
        return "customerTemplate/customer-update-order";
        }catch (Exception e)
        {
            redirectAttributes.addFlashAttribute("alertError", "Oops Some thing wrong!Can't connect to server! Try again!");
            return "redirect:/error";
        }
    }

    //update the order
    @RequestMapping(value="/myorder/order-detail/customer-update", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> updateDetailOrder(@RequestBody String jsonData,@CookieValue(name="token",defaultValue = "") String token) throws IOException {
        // Process the request data here...

        ObjectMapper objectMapper = new ObjectMapper();
        //get JSON from ajax
        Map<String, Object> data = objectMapper.readValue(jsonData, new TypeReference<Map<String,Object>>(){});
        // Return a response indicating success or failure
        Integer tableAmount= Integer.parseInt(data.get("table").toString());



        if(tableAmount<=0)
        {
            return new ResponseEntity<>("Choose the number of table!",HttpStatus.BAD_REQUEST);
        }

        //truyền order vào co' food id,service Id //xoa hết detail cu gắn với order. tạo mới lại orderdetai;
//     call api
        String url="http://localhost:8080/api/orders/update/order-detail/customer";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token);
        HttpEntity<?> entity = new HttpEntity<>(jsonData, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.PUT,
                entity,
                new ParameterizedTypeReference<String>() {
                }
        );

//getorder
        if(response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok("{\"message\": \"Congratulations on updating a successful dish and service!\"}");
        }
        else {
            return new ResponseEntity<>("fail!",HttpStatus.BAD_REQUEST);

            }
        }



//    @RequestMapping("/test")
//    public String test()
//    {
//        return ";
//    }

    //toJson add Map and List of venue
    public String toJson(List<VenueDTO> venues, List<VenueBooked> bookeds) {
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> combinedMap = new HashMap<>();

        // Convert venues to JSON
        String venuesJson;
        try {
            venuesJson = objectMapper.writeValueAsString(venues);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        combinedMap.put("venues", venuesJson);

        String bookedJson;
        try {
            bookedJson = objectMapper.writeValueAsString(bookeds);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
        combinedMap.put("bookeds", bookedJson);

        // Convert combinedMap to JSON
        try {
            return objectMapper.writeValueAsString(combinedMap);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
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

            //fix
            List<OrderDTO> ordersInMonth= new ArrayList<>();
            for (OrderDTO od:orderList)
            {
                if(
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


}


