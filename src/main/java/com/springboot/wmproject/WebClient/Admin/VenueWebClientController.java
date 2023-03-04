package com.springboot.wmproject.WebClient.Admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.springboot.wmproject.DTO.VenueDTO;
import com.springboot.wmproject.DTO.VenueImgDTO;
import com.springboot.wmproject.exceptions.GlobalExceptionHandler;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.exceptions.WmAPIException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui
        .Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class VenueWebClientController {
    @GetMapping("admin/venue")
    public String GetAllVenue(Model model,HttpServletRequest request) throws WmAPIException, ResourceNotFoundException {
        String msg=request.getParameter("msg");
        if(msg!=null){
            model.addAttribute("message",msg);
        }
        String uriVenue = "http://localhost:8080/api/venues/all";
        String uriVenueImg = "http://localhost:8080/api/venuesImg/all";
        RestTemplate restTemplate = new RestTemplate();
        List<VenueDTO> venueDTOList = restTemplate.getForObject(uriVenue, List.class);
        List<VenueImgDTO> venueImgDTOList = restTemplate.getForObject(uriVenueImg, List.class);
        ObjectMapper objectMapper = new ObjectMapper();
        model.addAttribute("list", objectMapper.convertValue(venueDTOList, new TypeReference<List<VenueDTO>>() {
        }));
        model.addAttribute("imgList", objectMapper.convertValue(venueImgDTOList, new TypeReference<List<VenueImgDTO>>() {
        }));
        return "adminTemplate/pages/gallery";
    }

    @GetMapping("admin/venue/delete")
    public String deleteVenue(HttpServletRequest request) throws WmAPIException, ResourceNotFoundException, IOException {
        String uriVenueImg = "http://localhost:8080/api/venuesImg/delete/{id}";
        RestTemplate restTemplate=new RestTemplate();
        int imgId = Integer.parseInt(request.getParameter("imgId"));
        if (imgId != 0) {
            restTemplate.delete(uriVenueImg,imgId);
        }
        return "redirect:/admin/venue?msg=Success'";
    }
    @GetMapping("admin/venue/create")
    public String createVenue(){
        return "";
    }
}
