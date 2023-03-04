//package com.springboot.wmproject.WebClient.ClientService;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.springboot.wmproject.DTO.OrderDTO;
//import com.springboot.wmproject.DTO.VenueDTO;
//import org.springframework.stereotype.Service;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.ArrayList;
//import java.util.List;
//@Service
//public class ClientService {
//    RestTemplate restTemplate ;
//
//    public ClientService(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//
//    public List<OrderDTO> findBydate(List<OrderDTO> list, String date)
//    {
//        List<OrderDTO> reponseList=new ArrayList<>();
//        for (OrderDTO order: list) {
//            if(order.getTimeHappen().contains(date))
//            {
//                reponseList.add(order);
//            }
//
//        }
//        return reponseList;
//    }
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
//
//
//
//}
