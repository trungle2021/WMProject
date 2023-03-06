package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui
        .Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import wm.clientmvc.DTO.VenueDTO;
import wm.clientmvc.DTO.VenueImgDTO;

import java.io.IOException;
import java.util.List;

@Controller
public class VenueWebClientController {
    @GetMapping("admin/venue")
    public String GetAllVenue(Model model,HttpServletRequest request) {
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
    public String deleteVenue(HttpServletRequest request){
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
