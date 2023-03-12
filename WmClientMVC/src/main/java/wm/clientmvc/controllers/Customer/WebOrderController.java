package wm.clientmvc.controllers.Customer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.ws.rs.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import wm.clientmvc.DTO.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

@Controller
@RequestMapping("/customer/order")
public class WebOrderController {

    RestTemplate restTemplate = new RestTemplate();
//    AuthService authService;

    String url = "http://localhost:8080/api/venues/all";
    String orderurl = "http://localhost:8080/api/order";


//    public WebOrderController(AuthService authService) {
//        this.authService = authService;
//    }

    @Autowired

//    String login="http://localhost:8080/api/auth/employee/login";


    @GetMapping("")
    public String order() {
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
                new ParameterizedTypeReference<List<VenueDTO>>() {
                }
        );

        List<VenueDTO> venueList = response.getBody();

        ResponseEntity<List<OrderDTO>> orderResponse = restTemplate.exchange(
                orderurl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<OrderDTO>>() {
                }
        );
        List<OrderDTO> orderList = orderResponse.getBody();
//        List find in date
        List<OrderDTO> bookedList = new ArrayList<>();
        if (orderList != null) {
            for (OrderDTO order : orderList) {
                if (order.getTimeHappen().contains(date)) {
                    bookedList.add(order);
                }
            }
        }
//        check venue available
        //fiter venue
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<VenueBooked> bookeds = new ArrayList<>();
        if (bookedList != null) {
//                int i=1;
            for (OrderDTO order : bookedList) {
                LocalTime timeHappen = LocalDateTime.parse(order.getTimeHappen(), formatter).toLocalTime();
                LocalTime compareTime1 = LocalTime.parse("13:00:00");
                LocalTime compareTime2 = LocalTime.parse("18:00:00");
                if (timeHappen.isBefore(compareTime1) && !order.getOrderStatus().equals("canceled")) {
                    VenueBooked newbooked = new VenueBooked();
//                newbooked.setBookedDay(date);
                    newbooked.setVenueId(String.valueOf(order.getVenueId()));
                    newbooked.setBookedTime("Afternoon");
                    bookeds.add(newbooked);
//                i++;
                } else if (timeHappen.isAfter(compareTime1) && timeHappen.isBefore(compareTime2) && !order.getOrderStatus().equals("canceled")) {
                    VenueBooked newbooked = new VenueBooked();
//                    newbooked.setBookedDay(date);
                    newbooked.setVenueId(String.valueOf(order.getVenueId()));
                    newbooked.setBookedTime("Evening");
                    bookeds.add(newbooked);
//                    i++;
                }
            }
        }
        String json = toJson(venueList, bookeds);
        return json;
    }

    @RequestMapping("/create")
    @ResponseBody
    public ResponseEntity<String> createOrder(@RequestBody String jsonData) throws JsonProcessingException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        ObjectMapper objectMapper = new ObjectMapper();
        //get JSON from ajax
        Map<String, String> data = objectMapper.readValue(jsonData, new TypeReference<Map<String, String>>() {
        });

        Integer venueId = Integer.parseInt(data.get("venueId"));
        String dateTime = new String();
        if (data.get("bookType").equalsIgnoreCase("Afternoon")) {
            dateTime = data.get("day") + " 12:00:00";
        } else if (data.get("bookType").equalsIgnoreCase("Evening")) {
            dateTime = data.get("day") + " 17:00:00";
        } else {
            throw new BadRequestException("Có Lỗi Xảy Ra!");
        }
        LocalDateTime orderDateTime = LocalDateTime.now();
        String formattedNow = orderDateTime.format(formatter);
        LocalDateTime happenDateTime = LocalDateTime.parse(dateTime, formatter);

//        LocalTime timeHappen = happenDateTime.toLocalTime();

        Duration duration = Duration.between(orderDateTime, happenDateTime);
        if (duration.toDays() >= 30 && happenDateTime.isAfter(orderDateTime)) {
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setBearerAuth(getToken());

            OrderDTO newOrder = new OrderDTO();
            newOrder.setVenueId(venueId);
            newOrder.setTimeHappen(dateTime);
            //set cung test
            newOrder.setCustomerId(1);
            //
            newOrder.setOrderStatus("Ordered");
            newOrder.setOrderDate(formattedNow);
            HttpEntity<OrderDTO> requestEntity = new HttpEntity<>(newOrder, httpHeaders);

            ResponseEntity<OrderDTO> responseEntity = restTemplate.postForEntity("http://localhost:8080/api/order/create", requestEntity, OrderDTO.class);

// Get the response body
            OrderDTO responseBody = responseEntity.getBody();

            return ResponseEntity.ok(objectMapper.writeValueAsString(responseBody));
        } else {

            return new ResponseEntity<>("Đặt Trước 30 ngày", HttpStatus.BAD_REQUEST);

        }
    }


    @RequestMapping(value = "/create-detail", method = RequestMethod.POST)

    public String createDetail(Model model, @RequestParam("orderId") int orderId) {
        String orderUrl = "http://localhost:8080/api/order/" + orderId;
//        model.addAttribute("orderId",orderId);


        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());
        HttpEntity<?> entity = new HttpEntity<>(headers);
//getorder

        ResponseEntity<OrderDTO> response = restTemplate.exchange(
                orderUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<OrderDTO>() {
                }

        );
        OrderDTO myOrder = response.getBody();
        //get foodlist
        String foodUrl = "http://localhost:8080/api/food";
        ResponseEntity<List<FoodDTO>> foodResponse = restTemplate.exchange(
                foodUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<FoodDTO>>() {
                }

        );
        List<FoodDTO> foodList = foodResponse.getBody();
        //get serviceList
        String serviceUrl = "http://localhost:8080/api/service";
        ResponseEntity<List<ServiceDTO>> serviceResponse = restTemplate.exchange(
                serviceUrl,
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<List<ServiceDTO>>() {
                }

        );
        List<ServiceDTO> serviceList = serviceResponse.getBody();
        model.addAttribute("myOrder", myOrder);
        model.addAttribute("foodList", foodList);
        model.addAttribute("serviceList", serviceList);


        return "neworder";
    }

    @RequestMapping(value = "/create-order", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createNewOrder(@RequestBody String jsonData) throws JsonProcessingException {
        // Process the request data here...

        ObjectMapper objectMapper = new ObjectMapper();
        //get JSON from ajax
        Map<String, Object> data = objectMapper.readValue(jsonData, new TypeReference<Map<String, Object>>() {
        });
        // Return a response indicating success or failure
        Integer orderId = Integer.parseInt(data.get("orderId").toString());
        List<String> foodData = objectMapper.readValue(objectMapper.writeValueAsString(data.get("foodList")), new TypeReference<List<String>>() {
        });
        List<String> svData = objectMapper.readValue(objectMapper.writeValueAsString(data.get("serviceList")), new TypeReference<List<String>>() {
        });


        String createFDUrl = "http://localhost:8080/api/foodDetail/create";
        String createSDUrl = "http://localhost:8080/api/servicedetail/create";
//        model.addAttribute("orderId",orderId);
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(getToken());

        for (String foodId : foodData) {
            FoodDetailDTO newFoodDetail = new FoodDetailDTO();
            newFoodDetail.setFoodId(Integer.parseInt(foodId));
            newFoodDetail.setOrderId(orderId);
            HttpEntity<?> entity = new HttpEntity<>(newFoodDetail, headers);

            ResponseEntity<FoodDetailDTO> response = restTemplate.postForEntity(
                    createFDUrl,
                    entity,
                    FoodDetailDTO.class);

        }

        for (String svId : svData) {
            ServiceDetailDTO newSVDetail = new ServiceDetailDTO();
            newSVDetail.setServiceId(Integer.parseInt(svId));
            newSVDetail.setOrderId(orderId);
            HttpEntity<?> entity = new HttpEntity<>(newSVDetail, headers);

            ResponseEntity<FoodDetailDTO> response = restTemplate.postForEntity(
                    createSDUrl,
                    entity,
                    FoodDetailDTO.class);

        }

//getorder


        return ResponseEntity.ok("{\"message\": \"Chọn Món Ăn và Dịch Vụ Thành Công!\"}");
    }


    //call login for token test
    public String getToken() {
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
//    public String checkVenueBooked(List<VenueDTO> venueList,OrderDTO order) {
//
//        for (VenueDTO venue : venueList) {
//            if (venue.getId() == order.getVenueId()) {
//
//                return "afternoon";
//            }
//        }
//        return "none";
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


}


