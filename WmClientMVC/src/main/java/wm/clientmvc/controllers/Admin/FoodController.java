package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.boot.Banner;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
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

import static wm.clientmvc.utils.Static_Status.*;

@Controller
@RequestMapping(value = "/staff/food")
public class FoodController {
    @GetMapping(value = "/index")
    public String getAllFood(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes,@ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError) {
        ParameterizedTypeReference<List<FoodDTO>> responseTypeFood = new ParameterizedTypeReference<List<FoodDTO>>() {
        };
        String msg = request.getParameter("msg");
        if (msg != null) {
            model.addAttribute("message", msg);
        }
    try{
        try {
            List<FoodDTO> foodDTOS = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeFood,
                    request,
                    response
            );
            Static_Status static_status = new Static_Status();
            List<String> foodType = new ArrayList<>();
            foodType.add(foodTypeMain);
            foodType.add(foodTypeStarter);
            foodType.add(foodTypeDessert);
            model.addAttribute("foodTypeList", foodType);
            model.addAttribute("foodList", foodDTOS);

            if (!alertMessage.isEmpty()) {
                model.addAttribute("alertMessage", alertMessage);
            }
            else {
                model.addAttribute("alertMessage", null);
            }
            if (!alertError.isEmpty()) {
                model.addAttribute("alertError", alertError);
            }
            else {
                model.addAttribute("alertError", null);
            }


            return "adminTemplate/pages/food/food";
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
                    return "redirect:/staff/food/index?msg=" + message;
            }
        }
    }catch(IOException e)
    {
        model.addAttribute("message", e.getMessage());
        return "adminTemplate/error";
    }

    }

    @GetMapping("/deleteImg")
    public String deleteFoodImg(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        int id = Integer.parseInt(request.getParameter("imgId"));
        if (id != 0) {
            try {
                APIHelper.makeApiCall(
                        SD_CLIENT.DOMAIN_APP_API + "/api/foodImgs/delete/" + id,
                        HttpMethod.DELETE,
                        null,
                        token,
                        String.class,request,response
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
                        return "redirect:/staff/food/index?msg=" + message;
                }
            }
        }
        return "redirect:/staff/food/index?msg=Success";
    }

    //remove material
    @PostMapping("/update/material")

    public String updateFood(@RequestParam("check") Boolean check, @ModelAttribute FoodDTO foodDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes, String material1, String unit1, String cost1, String material2, String unit2, String cost2, String material3, String unit3, String cost3, String code1, String code2, String code3, String removeMaterial) throws IOException {
        if (check != null) {
            foodDTO.setActive(check);
        }
        if (removeMaterial != null) {
            String[] arr = removeMaterial.split(",");
            for (int i = 1; i < arr.length; i++) {
                try {
                    APIHelper.makeApiCall(
                            SD_CLIENT.DOMAIN_APP_API + "/api/materials/delete/" + arr[i],
                            HttpMethod.DELETE,
                            null,
                            token,
                            String.class,request,response
                    );
                } catch (HttpClientErrorException ex) {
                    String responseError = ex.getResponseBodyAsString();
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(responseError, Map.class);
                    String message = map.get("message").toString();


            }

        }
        if (!material1.isEmpty() && !unit1.isEmpty() && !cost1.isEmpty() && !code1.isEmpty()) {
            try {
                MaterialDTO materialDTO = new MaterialDTO();
                materialDTO.setMaterialName(material1);
                materialDTO.setCount(Double.parseDouble(cost1));
                materialDTO.setUnit(unit1);
                materialDTO.setFoodId(foodDTO.getId());
                materialDTO.setMaterialCode(code1);
                APIHelper.makeApiCall(
                        SD_CLIENT.DOMAIN_APP_API + "/api/materials/create",
                        HttpMethod.POST,
                        materialDTO,
                        token,
                        MaterialDTO.class,request,response

                );

        //get alert
            attributes.addFlashAttribute("alertMessage", "Congratulation!Update succeess! ");

            return "redirect:/staff/food/index";
                } catch (Exception ex) {
            attributes.addFlashAttribute("alertError", "Oops Something wrong!");

            return "redirect:/staff/food/index" ;
                }


                APIHelper.makeApiCall(
                        SD_CLIENT.DOMAIN_APP_API + "/api/materials/create",
                        HttpMethod.POST,
                        materialDTO,
                        token,
                        MaterialDTO.class,request,response
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
                        return "redirect:/staff/food/index?msg=" + message;
                }
            }
        }
        if (!material3.isEmpty() && !unit3.isEmpty() && !cost3.isEmpty() && !code3.isEmpty()) {
            try {
                MaterialDTO materialDTO = new MaterialDTO();
                materialDTO.setMaterialName(material3);
                materialDTO.setCount(Double.parseDouble(cost3));
                materialDTO.setUnit(unit3);
                materialDTO.setFoodId(foodDTO.getId());
                materialDTO.setMaterialCode(code3);

                APIHelper.makeApiCall(
                        SD_CLIENT.DOMAIN_APP_API + "/api/materials/create",
                        HttpMethod.POST,
                        materialDTO,
                        token,
                        MaterialDTO.class,request,response
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
                        return "redirect:/staff/food/index?msg=" + message;
                }
            }
        }

        try {
            FoodDTO foodDTO = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/getOne/" + foodId,
                    HttpMethod.GET,
                    null,
                    token,
                    FoodDTO.class,request,response
            );

            model.addAttribute("stater",foodTypeStarter);
            model.addAttribute("main",foodTypeMain);
            model.addAttribute("dessert",foodTypeDessert);
            model.addAttribute("foodDTO", foodDTO);
            return "adminTemplate/pages/food/food-detail";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertError", "Oops Something wrong!");

            return "redirect:/staff/food/index" ;
        }



    }
    @RequestMapping(value ="/detail-food/update-food",method = RequestMethod.POST)
    public  String foodUpdate(@CookieValue(name="token",defaultValue = "")String token,Model model,@Valid @ModelAttribute FoodDTO foodDTO,BindingResult bindingResult,RedirectAttributes redirectAttributes)
    {

        if (bindingResult.hasErrors()) {

            model.addAttribute("stater",foodTypeStarter);
            model.addAttribute("main",foodTypeMain);
            model.addAttribute("dessert",foodTypeDessert);
            model.addAttribute("foodDTO",foodDTO);

            return "adminTemplate/pages/food/food-detail";

        }
        FoodDTO  editedFood;
        try {
           editedFood = APIHelper.makeApiCall(
             SD_CLIENT.DOMAIN_APP_API + "/api/food/update",
             HttpMethod.PUT,
             foodDTO,
             token,
             FoodDTO.class
     );
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("alertError", "Oops Something wrong!");

            return "redirect:/staff/food/index" ;

        }
        if (editedFood!=null) {
                redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Update succeess! ");
            }
            return "redirect:/staff/food/index";
    }
    @RequestMapping(value ="/detail-food/active-food",method = RequestMethod.POST)
    public  String foodActive(@CookieValue(name="token",defaultValue = "")String token, @PathParam("foodId")Integer foodId,@PathParam("foodActive") String foodActive,  RedirectAttributes redirectAttributes) {
            boolean myActive=Boolean.parseBoolean(foodActive);
        FoodDTO foodDTO= new FoodDTO();
        foodDTO.setId(foodId);
        foodDTO.setActive(!myActive);
        try {
            try {
               APIHelper.makeApiCall(
                        SD_CLIENT.DOMAIN_APP_API + "/api/food/update/active",
                        HttpMethod.PUT,
                        foodDTO,
                        token,
                        FoodDTO.class
                );


            redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!! ");
            return "redirect:/staff/food/index";
        } catch (HttpClientErrorException e) {
            String responseError = e.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();
                redirectAttributes.addFlashAttribute("alertError", message);
                return "redirect:/staff/food/index" ;
        }
        } catch (IOException e) {
            redirectAttributes.addFlashAttribute("alertError", "Oops Something wrong!");

            return "redirect:/staff/food/index" ;
        }


    }

//addmaterial
    //khang ajax
    @RequestMapping(value="/addMaterial",method=RequestMethod.POST)
    public ResponseEntity<String> addMaterialforFood(@CookieValue(name="token",defaultValue = "")String token, Model model,@RequestBody String jsonData) throws IOException {
        String url=SD_CLIENT.DOMAIN_APP_API+"/api/materialDetails/create";

        ObjectMapper objectMapper = new ObjectMapper();
        //get JSON from ajax
        Map<String, Object> data = objectMapper.readValue(jsonData, new TypeReference<Map<String,Object>>(){});

        if(!data.get("foodId").toString().isEmpty() && !data.get("materialId").toString().isEmpty() && !data.get("materialCount").toString().isEmpty())
        {

            Integer foodId= Integer.parseInt(data.get("foodId").toString());
        Integer materialId=Integer.parseInt(data.get("materialId").toString());
        Integer count=Integer.parseInt(data.get("materialCount").toString());
        MaterialDetailDTO materialDetailDTO=new MaterialDetailDTO();
        materialDetailDTO.setMaterialId(materialId);
        materialDetailDTO.setFoodId(foodId);
        materialDetailDTO.setCount(count);

        APIHelper.makeApiCall(
                url,
                HttpMethod.POST,
                materialDetailDTO,
                token,
                OrderDTO.class
        );

        return ResponseEntity.ok("{\"message\": \"Congratulations!Add Material success\"}");
        }
        else{
            return new ResponseEntity<>("{\"message\": \"Fail, make sure you all feild is not empty\"}", HttpStatus.BAD_REQUEST);

        }

    }

    @PostMapping("/foodImg/create")
    public String createFoodImg(@RequestParam("imagePageId") String foodImageDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes, @RequestParam("foodImgFiles") MultipartFile[] files) throws IOException {
        ClientUtilFunction utilFunction = new ClientUtilFunction();
        List<String> foodImgUrls = utilFunction.AddMultipleFilesEncrypted(files);
        List<FoodImageDTO> newList = new ArrayList<>();
        if (foodImageDTO != null) {
            for (String item : foodImgUrls
            ) {
                FoodImageDTO newFoodImageDTO = new FoodImageDTO();
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
                    responseTypeVenue,request,response
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
                    return "redirect:/staff/food/index?msg=Fails";
            }
        }
        return "redirect:/staff/food/index?msg=Success";
    }

    @PostMapping("/create")
    public String createFood( @ModelAttribute FoodDTO foodDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {

        foodDTO.setActive(false);
        try {
            FoodDTO data = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/create",
                    HttpMethod.POST,
                    foodDTO,
                    token,
                    FoodDTO.class,request,response
            );
            if (data == null) {
                return "redirect:/staff/food/index?msg=Fails";
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
                    return "redirect:/staff/food/index?msg=" + message;
            }
        }
        return "redirect:/staff/food/index";
    }
//show material
    @GetMapping("/material")
    public String materialDetail(@RequestParam("foodId") String id, Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {

        try {
            FoodDTO foodDTO = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/getOne/"+id,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeFood,request,response

            );

            ParameterizedTypeReference<List<MaterialDTO>> reference=new ParameterizedTypeReference<List<MaterialDTO>>() {};
            String url=SD_CLIENT.DOMAIN_APP_API+"/api/materials/all";

                  List<MaterialDTO> listMaterial= APIHelper.makeApiCall(
                url,
                HttpMethod.GET,
                null,
                token,
                reference
                );

            model.addAttribute("materialList",listMaterial);
            model.addAttribute("foodDTO", foodDTO);
            return "adminTemplate/pages/food/material";
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
                    return "redirect:/staff/food/index?msg=Fails";
            }
        }
    }

    @GetMapping("/foodImg")
    public String foodImg(@RequestParam("foodId") String id, Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) throws IOException {
        model.addAttribute("foodId", id);
        ParameterizedTypeReference<List<FoodDTO>> responseTypeFood = new ParameterizedTypeReference<List<FoodDTO>>() {
        };
        try {
            List<FoodDTO> foodDTOS = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/food/all",
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeFood,request,response
            );
            model.addAttribute("foodId", Integer.parseInt(id));
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
                default:
                    return "redirect:/staff/food/index?msg=Fails";
            }
        }
        return "adminTemplate/pages/food/foodpic";
    }

}
