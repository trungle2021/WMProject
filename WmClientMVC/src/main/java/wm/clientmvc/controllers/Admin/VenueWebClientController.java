package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui
        .Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.*;
import wm.clientmvc.entities.VenueImages;
import wm.clientmvc.entities.Venues;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/staff")
public class VenueWebClientController {


    @GetMapping("/venue")
    public String GetAllVenue(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        ParameterizedTypeReference<List<VenueDTO>> responseTypeVenue = new ParameterizedTypeReference<List<VenueDTO>>() {
        };
        ParameterizedTypeReference<List<VenueImgDTO>> responseTypeVenueImg = new ParameterizedTypeReference<List<VenueImgDTO>>() {
        };

        String msg = request.getParameter("msg");
        if (msg != null) {
            model.addAttribute("message", msg);
        }

        try {
            List<VenueDTO> venueDTOList = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venues/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeVenue
            );
            List<VenueImgDTO> venueImgDTOList = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venuesImg/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeVenueImg
            );
            model.addAttribute("list", venueDTOList);
            model.addAttribute("imgList", venueImgDTOList);
        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                case "403":
                    return "redirect:/access-denied";

            }
        }
        return "adminTemplate/pages/gallery";
    }


    @GetMapping("/venue/delete")
    public String deleteVenue(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        int id = Integer.parseInt(request.getParameter("imgId"));
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venuesImg/delete/" + id,
                    HttpMethod.DELETE,
                    null,
                    token,
                    String.class
            );
        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                case "403":
                    return "redirect:/access-denied";

            }
        }
        return "redirect:/staff/venue?msg=Success";
    }

    @PostMapping("/venue/create")
    public String createVenue(@ModelAttribute VenueDTO venueDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venues/create",
                    HttpMethod.POST,
                    venueDTO,
                    token,
                    VenueDTO.class
            );
        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                case "403":
                    return "redirect:/access-denied";

            }
        }
        return "redirect:/staff/venue?msg=Success";
    }

    @PostMapping("/venueImg/create")
    public String createVenueImg(@ModelAttribute VenueImgDTO venueImgDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes, @RequestParam("create-multiple-picture") MultipartFile[] files) throws IOException {
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        List<String> venueImgUrls = utilFunction.AddMultipleFilesEncrypted(files);

        List<VenueImgDTO> newList = new ArrayList<>();
        if (venueImgDTO != null) {
            for (String venueImgUrl : venueImgUrls
            ) {
                VenueImgDTO newVenueImages = new VenueImgDTO();
                newVenueImages.setVenueId(venueImgDTO.getVenueId());
                newVenueImages.setUrl(venueImgUrl);
                newList.add(newVenueImages);
            }
        }
        ParameterizedTypeReference<List<VenueImgDTO>> responseTypeVenue = new ParameterizedTypeReference<List<VenueImgDTO>>() {
        };
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venuesImg/creates",
                    HttpMethod.POST,
                    newList,
                    token,
                    responseTypeVenue
            );
        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                case "403":
                    return "redirect:/access-denied";

            }
        }
        return "redirect:/staff/venue?msg=Success";
    }

    @PostMapping("/venue/update")
    public String updateVenue(@ModelAttribute VenueDTO venueDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venues/update",
                    HttpMethod.PUT,
                    venueDTO,
                    token,
                    VenueDTO.class
            );
        } catch (HttpClientErrorException ex) {
            String responseError = ex.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();

            String status = String.valueOf(ex.getStatusCode().value());
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                case "403":
                    return "redirect:/access-denied";

            }
        }
        return "redirect:/staff/venue?msg=Success";
    }
}
