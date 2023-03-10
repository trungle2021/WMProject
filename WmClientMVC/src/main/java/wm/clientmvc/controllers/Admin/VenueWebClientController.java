package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui
        .Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import wm.clientmvc.DTO.VenueDTO;
import wm.clientmvc.DTO.VenueImgDTO;
import wm.clientmvc.entities.VenueImages;
import wm.clientmvc.entities.Venues;
import wm.clientmvc.utils.ClientUtilFunction;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VenueWebClientController {


    @GetMapping("admin/venue")
    public String GetAllVenue(Model model, HttpServletRequest request) {
        String msg = request.getParameter("msg");
        if (msg != null) {
            model.addAttribute("message", msg);
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
    public String deleteVenue(HttpServletRequest request) {
        String uriVenueImg = "http://localhost:8080/api/venuesImg/delete/{id}";
        RestTemplate restTemplate = new RestTemplate();
        int imgId = Integer.parseInt(request.getParameter("imgId"));
        if (imgId != 0) {
            restTemplate.delete(uriVenueImg, imgId);
        }
        return "redirect:/admin/venue?msg=Success";
    }

    @PostMapping("admin/venue/create")
    public String createVenue(@ModelAttribute VenueDTO venueDTO, @RequestParam("create-multiple-picture") MultipartFile[] files) {
        String uriVenue = "http://localhost:8080/api/venues/create";
        RestTemplate restTemplate = new RestTemplate();

//        ClientUtilFunction utilFunction= new ClientUtilFunction();
//        Path absolutePath= Paths.get("WmClientMVC/src/main/resources/static/images/venue_img/add_venue");
//        String venueImgUrl =utilFunction.AddMultipleFilesEncrypted(files,absolutePath.toAbsolutePath());

        if (venueDTO != null) {
            Venues newVenues = new Venues();
            newVenues.setVenueName(venueDTO.getVenueName());
            newVenues.setMinPeople(venueDTO.getMinPeople());
            newVenues.setMaxPeople(venueDTO.getMaxPeople());
            newVenues.setPrice(venueDTO.getPrice());
            restTemplate.postForEntity(uriVenue, newVenues, String.class);
        }
        return "redirect:/admin/venue?msg=Success";
    }

    @PostMapping("admin/venueImg/create")
    public String createVenueImg(@ModelAttribute VenueImgDTO venueImgDTO, @RequestParam("create-multiple-picture") MultipartFile[] files) {
        String uriVenue = "http://localhost:8080/api/venuesImg/creates";
        RestTemplate restTemplate = new RestTemplate();

        ClientUtilFunction utilFunction = new ClientUtilFunction();
        List<String> venueImgUrls = utilFunction.AddMultipleFilesEncrypted(files);
        List<VenueImages> newList=new ArrayList<>();
        if (venueImgDTO != null) {
            for (String venueImgUrl : venueImgUrls
            ) {
                VenueImages newVenueImages=new VenueImages();
                newVenueImages.setVenueId(venueImgDTO.getVenueId());
                newVenueImages.setUrl(venueImgUrl);
                newList.add(newVenueImages);
            }
        }
        restTemplate.postForEntity(uriVenue,newList,String.class);
        return "redirect:/admin/venue?msg=Success";
    }
    @PostMapping("admin/venue/update")
    public String updateVenue(@ModelAttribute VenueDTO venueDTO)  {
        String uriVenue = "http://localhost:8080/api/venues/update";
        RestTemplate restTemplate = new RestTemplate();
        Venues venues=new Venues();
        venues.setId(venueDTO.getId());
        venues.setVenueName(venueDTO.getVenueName());
        venues.setMinPeople(venueDTO.getMinPeople());
        venues.setMaxPeople(venueDTO.getMaxPeople());
        venues.setPrice(venueDTO.getPrice());
        restTemplate.put(uriVenue,venues);
        return "redirect:/admin/venue?msg=Success";
    }
}
