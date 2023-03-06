package wm.clientmvc.controllers.Customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import wm.clientmvc.DTO.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/order")
public class WebOrderController {

    RestTemplate restTemplate = new RestTemplate();
//    AuthService authService;

    String url = "http://localhost:8080/api/venues/all";
    String orderurl="http://localhost:8080/api/order";


//    public WebOrderController(AuthService authService) {
//        this.authService = authService;
//    }

    @Autowired

//    String login="http://localhost:8080/api/auth/employee/login";



    @GetMapping("")
        public String order(){
            return "orderpage";
        }

        @RequestMapping("/getvenue")
        @ResponseBody
        public String showVenue(@RequestBody String date) throws JsonProcessingException {
            Map<String, Object> map = new HashMap<>();
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(getToken());

            HttpEntity<?> entity = new HttpEntity<>(headers);

            ResponseEntity<List<VenueDTO>> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<List<VenueDTO>>() {}
            );

            List<VenueDTO> venueList = response.getBody();

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
                if (order.getTimeHappen().contains(date)) {
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
                if(timeHappen.isBefore(compareTime1) && !order.getOrderStatus().equals("canceled")){
                VenueBooked newbooked=new VenueBooked();
//                newbooked.setBookedDay(date);
                newbooked.setVenueId(String.valueOf(order.getVenueId()));
                newbooked.setBookedTime("Afternoon");
                bookeds.add(newbooked);
//                i++;
                }
                else if(timeHappen.isAfter(compareTime1) && timeHappen.isBefore(compareTime2) && !order.getOrderStatus().equals("canceled")){
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
            return json;
    }

    @RequestMapping("/create")
    @ResponseBody
    public String createOrder(@RequestBody String jsonData) throws JsonProcessingException {

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
        else{ return "error";}

        HttpHeaders httpHeaders= new HttpHeaders();
         httpHeaders.setBearerAuth(getToken());

        OrderDTO newOrder=new OrderDTO();
        newOrder.setVenueId(venueId);
        newOrder.setTimeHappen(dateTime);
        //set cung test
        newOrder.setCustomerId(1);
        //
        newOrder.setOrderStatus("Ordered");
        newOrder.setOrderDate(LocalDateTime.now().toString());
        HttpEntity<OrderDTO> requestEntity= new HttpEntity<>(newOrder,httpHeaders);

        ResponseEntity<OrderDTO> responseEntity = restTemplate.postForEntity("http://localhost:8080/api/order/create", requestEntity, OrderDTO.class);

// Get the response body
        OrderDTO responseBody = responseEntity.getBody();
        return objectMapper.writeValueAsString(responseBody);
    }





//call login for token test
public String getToken()
{
    RestTemplate restTemplate = new RestTemplate();
    String loginUrl = "http://localhost:8080/api/auth/employee/login";
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    LoginDTO loginRequest = new LoginDTO();
    loginRequest.setUsername("admin");
    loginRequest.setPassword("admin");

    HttpEntity<LoginDTO> request = new HttpEntity<>(loginRequest, headers);
    ResponseEntity<JWTAuthResponse> response = restTemplate.postForEntity(loginUrl, request, JWTAuthResponse.class);
    JWTAuthResponse jwtAuthResponse = response.getBody();
    String token = jwtAuthResponse.getAccessToken();
    return token;
}
//test
    public String checkVenueBooked(List<VenueDTO> venueList,OrderDTO order) {

        for (VenueDTO venue : venueList) {
            if (venue.getId() == order.getVenueId()) {

                return "afternoon";
            }
        }
        return "none";
    }

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




}


