package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.*;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.ClientUtilFunction;
import wm.clientmvc.utils.SD_CLIENT;
import wm.clientmvc.utils.Static_Status;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/staff/food")
public class FoodController {
    @GetMapping(value = "/index")
    public String getAllFood(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        ParameterizedTypeReference<List<FoodDTO>> responseTypeFood = new ParameterizedTypeReference<List<FoodDTO>>() {
        };
        String msg = request.getParameter("msg");
        if (msg != null) {
            model.addAttribute("message", msg);
        }
        try {
            List<FoodDTO> foodDTOS = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeFood
            );
            Static_Status static_status=new Static_Status();
            List<String> foodType=new ArrayList<>();
            foodType.add(static_status.foodTypeMain);
            foodType.add(static_status.foodTypeStarter);
            foodType.add(static_status.foodTypeDessert);
            model.addAttribute("foodTypeList",foodType);
            model.addAttribute("foodList", foodDTOS);
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
        return "adminTemplate/food";
    }
    @GetMapping("/deleteImg")
    public String deleteFoodImg(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        int id=Integer.parseInt(request.getParameter("imgId"));
        if(id != 0){
            try {
                APIHelper.makeApiCall(
                        SD_CLIENT.DOMAIN_APP_API + "/api/foodImgs/delete/" + id,
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
        }
        return "redirect:/staff/food/index?msg=Success";
    }
    @PostMapping("/update/material")
    public String updateFood(@ModelAttribute FoodDTO foodDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes, String material1, String unit1, String cost1, String material2, String unit2, String cost2, String material3, String unit3, String cost3, String removeMaterial) throws IOException {

        if(removeMaterial!=null){
            String[] arr=removeMaterial.split(",");
            for (int i=1;i<arr.length;i++){
                try {
                    APIHelper.makeApiCall(
                            SD_CLIENT.DOMAIN_APP_API + "/api/materials/delete/" + arr[i],
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
            }
        }
         if(!material1.isEmpty() && !unit1.isEmpty() && !cost1.isEmpty()){
            try {
                MaterialDTO materialDTO=new MaterialDTO();
                materialDTO.setMaterialName(material1);
                materialDTO.setCost(Double.parseDouble(cost1));
                materialDTO.setUnit(unit1);
                materialDTO.setFoodId(foodDTO.getId());
                APIHelper.makeApiCall(
                        SD_CLIENT.DOMAIN_APP_API + "/api/materials/create",
                        HttpMethod.POST,
                        materialDTO,
                        token,
                        MaterialDTO.class
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
        }
         if(!material2.isEmpty() && !unit2.isEmpty() && !cost2.isEmpty()){
            try {
                MaterialDTO materialDTO=new MaterialDTO();
                materialDTO.setMaterialName(material2);
                materialDTO.setCost(Double.parseDouble(cost2));
                materialDTO.setUnit(unit2);
                materialDTO.setFoodId(foodDTO.getId());
                APIHelper.makeApiCall(
                        SD_CLIENT.DOMAIN_APP_API + "/api/materials/create",
                        HttpMethod.POST,
                        materialDTO,
                        token,
                        MaterialDTO.class
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
        }
         if(!material3.isEmpty() && !unit3.isEmpty() && !cost3.isEmpty()){
            try {
                MaterialDTO materialDTO=new MaterialDTO();
                materialDTO.setMaterialName(material3);
                materialDTO.setCost(Double.parseDouble(cost3));
                materialDTO.setUnit(unit3);
                materialDTO.setFoodId(foodDTO.getId());
                APIHelper.makeApiCall(
                        SD_CLIENT.DOMAIN_APP_API + "/api/materials/create",
                        HttpMethod.POST,
                        materialDTO,
                        token,
                        MaterialDTO.class
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
        }
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/update",
                    HttpMethod.PUT,
                    foodDTO,
                    token,
                    FoodDTO.class
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

        return "redirect:/staff/food/index?msg=Success";
    }
    @PostMapping("/foodImg/create")
    public String createFoodImg(@RequestParam("imagePageId")String foodImageDTO,@CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes, @RequestParam("foodImgFiles") MultipartFile[] files) throws IOException {
        ClientUtilFunction utilFunction=new ClientUtilFunction();
        List<String> foodImgUrls=utilFunction.AddMultipleFilesEncrypted(files);
        List<FoodImageDTO> newList=new ArrayList<>();
        if(foodImageDTO!=null){
            for (String item:foodImgUrls
                 ) {
                FoodImageDTO newFoodImageDTO=new FoodImageDTO();
                newFoodImageDTO.setUrl(item);
                newFoodImageDTO.setFoodId(Integer.parseInt(foodImageDTO));
                newList.add(newFoodImageDTO);
            }
        }
        ParameterizedTypeReference<List<FoodImageDTO>> responseTypeVenue = new ParameterizedTypeReference<List<FoodImageDTO>>() {
        };
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/foodImgs/creates",
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
        return "redirect:/staff/food/index?msg=Success";
    }
    @PostMapping("/create")
    public String createFood(@ModelAttribute FoodDTO foodDTO,@CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        try {
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/create",
                    HttpMethod.POST,
                    foodDTO,
                    token,
                    FoodDTO.class
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
        return "redirect:/staff/food/index?msg=Success";
    }
    @GetMapping("/material")
    public String materialDetail(@RequestParam("foodId")String id,Model model,@CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        model.addAttribute("foodId",id);
        ParameterizedTypeReference<List<FoodDTO>> responseTypeFood = new ParameterizedTypeReference<List<FoodDTO>>() {
        };
        try {
            List<FoodDTO> foodDTOS = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeFood
            );
            Static_Status static_status=new Static_Status();
            List<String> foodType=new ArrayList<>();
            foodType.add(static_status.foodTypeMain);
            foodType.add(static_status.foodTypeStarter);
            foodType.add(static_status.foodTypeDessert);
            model.addAttribute("foodId",Integer.parseInt(id));
            model.addAttribute("foodTypeList",foodType);
            model.addAttribute("foodList", foodDTOS);
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
        return "adminTemplate/material";
    }
    @GetMapping("/foodImg")
    public String foodImg(@RequestParam("foodId")String id,Model model,@CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        model.addAttribute("foodId",id);
        ParameterizedTypeReference<List<FoodDTO>> responseTypeFood = new ParameterizedTypeReference<List<FoodDTO>>() {
        };
        try {
            List<FoodDTO> foodDTOS = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeFood
            );
            model.addAttribute("foodId",Integer.parseInt(id));
            model.addAttribute("foodList", foodDTOS);
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
        return "adminTemplate/foodpic";
    }

}
