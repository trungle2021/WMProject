package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
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
@RequestMapping("/staff/venues")
public class VenueWebClientController {


    @GetMapping("")
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
                    SD_CLIENT.DOMAIN_APP_API + "/api/venuesImgs/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeVenueImg
            );
            for (VenueDTO item : venueDTOList
            ) {
                item.setVenueImagesById(null);
            }

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


    @GetMapping("/delete")
    public String deleteVenue(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        int id = Integer.parseInt(request.getParameter("imgId"));
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venuesImgs/delete/" + id,
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
                default:
                    return "redirect:/staff/venues?msg=" + message;
            }
        }
        return "redirect:/staff/venues?msg=Success";
    }

    @PostMapping("/create")
    public String createVenue(@ModelAttribute VenueDTO venueDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes, @RequestParam("checkCreate") Boolean checkCreate) throws IOException {
        if (checkCreate != null) {
            venueDTO.setActive(checkCreate);
        }
        try {
            VenueDTO data = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venues/create",
                    HttpMethod.POST,
                    venueDTO,
                    token,
                    VenueDTO.class
            );
            if (data == null) {
                return "redirect:/staff/venues?msg=Fail";
            }
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
                default:
                    return "redirect:/staff/venues?msg=" + message;
            }

        }
        return "redirect:/staff/venues?msg=Success";
    }

    @PostMapping("/venueImgs/create")
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
                    SD_CLIENT.DOMAIN_APP_API + "/api/venuesImgs/creates",
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
                default:
                    return "redirect:/staff/venues?msg=" + message;
            }
        }
        return "redirect:/staff/venues?msg=Success";
    }

    @PostMapping("/update")
    public String updateVenue(@RequestParam("check") Boolean checkActive, @ModelAttribute VenueDTO venueDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        if (checkActive != null) {
            venueDTO.setActive(checkActive);
        }
        try {
            VenueDTO data = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/venues/update",
                    HttpMethod.PUT,
                    venueDTO,
                    token,
                    VenueDTO.class
            );
            if (data == null) {
                return "redirect:/staff/venues?msg=Fail";
            }
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
                default:
                    return "redirect:/staff/venues?msg=" + message;
            }
        }
        return "redirect:/staff/venues?msg=Success";
    }
}
