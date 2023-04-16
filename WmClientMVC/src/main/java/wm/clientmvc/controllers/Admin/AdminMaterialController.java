package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.ws.rs.PathParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.MaterialDTO;
import wm.clientmvc.DTO.MaterialDetailDTO;
import wm.clientmvc.DTO.OrderDTO;
import wm.clientmvc.DTO.ServiceDTO;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static wm.clientmvc.utils.Static_Status.orderStatusConfirm;

@Controller
@RequestMapping("/staff/materials")
public class AdminMaterialController {

    @RequestMapping
    public String materialIndex(Model model,@ModelAttribute("alertMessage") String alertMessage)
    {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now =  LocalDateTime.now();
        String today=now.format(formatter);
        model.addAttribute("today",today);

        if (!alertMessage.isEmpty()) {
            model.addAttribute("alertMessage", alertMessage);
        }
        else {
            model.addAttribute("alertMessage", null);
        }
        return "adminTemplate/pages/organize/material-index";
    }

    @RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
    public String showMaterialbyOrder(Model model, @PathVariable Integer id, @CookieValue(name="token",defaultValue = "")String token, HttpServletRequest request, HttpServletResponse response)
    {
        //get order
        List<OrderDTO>list= new ArrayList<>();

//    ParameterizedTypeReference orderResponseType= new ParameterizedTypeReference() {};

        try {
            String oUrl="http://localhost:8080/api/orders/"+id;
            OrderDTO order=   APIHelper.makeApiCall(
                    oUrl,
                    HttpMethod.GET,
                    null,
                    token,
                    OrderDTO.class,request,response
            );
            list.add(order);



            //get material
            ParameterizedTypeReference<List<MaterialDetailDTO>> responseType=new ParameterizedTypeReference<List<MaterialDetailDTO>>() {};
            String url="http://localhost:8080/api/materialDetails/byOrder/"+id;


            List<MaterialDetailDTO> materialList= APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType,request,response
            );
            //getday
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now =  LocalDateTime.now();
            String today=now.format(formatter);
            if(materialList!=null){
                for (MaterialDetailDTO material:materialList)
                {
                    material.setCount(material.getCount()*order.getTableAmount());
                }}

//        totalMaterial;
            model.addAttribute("today",today);
            model.addAttribute("orderList",list);
            model.addAttribute("materials",materialList);
            return "adminTemplate/pages/organize/material";
        } catch (Exception e) {
            model.addAttribute("message",e.getMessage());
            return "adminTemplate/error";
        }
    }

    @RequestMapping(value="/detail/searchId",method = RequestMethod.POST)
    public String materialSearchId(Model model,@PathParam("orderId")Integer orderId, @CookieValue(name="token",defaultValue = "")String token,RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response)
    {
//check team

        //
        //get order
        List<OrderDTO>list= new ArrayList<>();

//    ParameterizedTypeReference orderResponseType= new ParameterizedTypeReference() {};
//
        try {

            String oUrl="http://localhost:8080/api/orders/"+orderId;
            OrderDTO order=   APIHelper.makeApiCall(
                    oUrl,
                    HttpMethod.GET,
                    null,
                    token,
                    OrderDTO.class,request,response
            );
            list.add(order);
            //get material


            ParameterizedTypeReference<List<MaterialDetailDTO>> responseType=new ParameterizedTypeReference<List<MaterialDetailDTO>>() {};
            String url="http://localhost:8080/api/materialDetails/byOrder/"+orderId;

            List<MaterialDetailDTO>   materialList= APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType,request,response
            );



            //getday
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now =  LocalDateTime.now();
            String today=now.format(formatter);
            if(materialList!=null){
                for (MaterialDetailDTO material:materialList)
                {
                    material.setCount(material.getCount()*order.getTableAmount());
                }
            }else {
                redirectAttributes.addFlashAttribute("alertMessage", "Cant found material!Try Again! ");
                return "redirect:/staff/materials";
            }

//        totalMaterial;
            model.addAttribute("today",today);
            model.addAttribute("orderList",list);
            model.addAttribute("materials",materialList);
            return "adminTemplate/pages/organize/material";


        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertMessage", "Cant found material!Try Again! ");
            return "redirect:/staff/materials";
        }
    }

    @RequestMapping(value="/detail/searchDate",method = RequestMethod.POST)
    public String materialSearchDate(Model model, @PathParam("date")String date, @CookieValue(name="token",defaultValue = "")String token, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response)
    {
        //check team

        //get order
        try {

            //getday
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now =  LocalDateTime.now();
            String today=now.format(formatter);
            //get empId
            Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
            String role=authentication.getAuthorities().stream().findFirst().toString();

            //role admin
            if(role.contains("ADMIN")) {
                ParameterizedTypeReference<List<OrderDTO>> orderResponseType = new ParameterizedTypeReference<>() {};
                //confirm only
                String ourl = "http://localhost:8080/api/orders/byStatus/confirm";

                List<OrderDTO> list=getOrderList(token,ourl,date,request,response);
                if(list==null || list.isEmpty())
                {
                    redirectAttributes.addFlashAttribute("alertMessage", "No confirm order found in this day! ");
                    return "redirect:/staff/materials";
                }

                //get material in day with count
                List<MaterialDetailDTO> materialList=getMaterialList(token,list,request,response);

                model.addAttribute("today",today);
                model.addAttribute("orderList",list);
                model.addAttribute("materials",materialList);
                return "adminTemplate/pages/organize/material";
                //role ORGANIZE
            }else if(role.contains("ORGANIZE")){
                CustomUserDetails empUserDetails= (CustomUserDetails) authentication.getPrincipal();
                Long empId= empUserDetails.getUserId();

                //confirm only
                String ourl = "http://localhost:8080/api/orders/byTeam/empId/"+empId;
//
//get list of today
                List<OrderDTO>list=getOrderList(token,ourl,date,request,response);
//
                List<MaterialDetailDTO> materialList=getMaterialList(token,list,request,response);

//        totalMaterial;
                model.addAttribute("today",today);
                model.addAttribute("orderList",list);
                model.addAttribute("materials",materialList);
                return "adminTemplate/pages/organize/material";
            }
            else{
                redirectAttributes.addFlashAttribute("alertMessage", "You not allow to Access this action! ");
                return "redirect:/staff/materials";

            }


        } catch (Exception e) {
            model.addAttribute("message",e.getMessage());
            return "adminTemplate/error";
        }
    }
    //
//new
    @RequestMapping("/showAll")
    public String showAllMaterial (Model model, @CookieValue(name = "token", defaultValue = "") String token, @ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError, HttpServletRequest request, HttpServletResponse response)
    {
        ParameterizedTypeReference<List<MaterialDTO>> typeReference=new ParameterizedTypeReference<List<MaterialDTO>>() {};
        String url=SD_CLIENT.DOMAIN_APP_API+"/api/materials/all";
        try {
            List<MaterialDTO> list =APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    typeReference,request,response

            );
            //reverse
            Collections.reverse(list);
            model.addAttribute("list",list);

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


            return "adminTemplate/pages/materials/show_materials";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "adminTemplate/error";
        }
    }
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model,@ModelAttribute("alertError") String alertError) {
        //tao đối tượng cho view
        model.addAttribute("materialDTO", new MaterialDTO());

        if (!alertError.isEmpty()) {
            model.addAttribute("alertError", alertError);
        }
        else {
            model.addAttribute("alertError", null);
        }

        return "adminTemplate/pages/materials/add-material";
    }

    @RequestMapping(value = "/create-material", method = RequestMethod.POST)
    public String create(Model model, @Validated @ModelAttribute MaterialDTO materialDTO, BindingResult bindingResult, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response)
    {
        if (bindingResult.hasErrors()) {

            model.addAttribute("materialDTO",materialDTO);

            return "adminTemplate/pages/materials/add-material";

        } else {
            try {
                try{
                    MaterialDTO material = APIHelper.makeApiCall(
                            SD_CLIENT.DOMAIN_APP_API + "/api/materials/create",
                            HttpMethod.POST,
                            materialDTO,
                            token,
                            MaterialDTO.class,request,response
                    );


                    redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Create Material Success!! ");
                    return "redirect:/staff/materials/showAll";

                }catch (HttpClientErrorException e) {
                    String responseError = e.getResponseBodyAsString();
                    ObjectMapper mapper = new ObjectMapper();
                    Map<String, Object> map = mapper.readValue(responseError, Map.class);
                    String message = map.get("message").toString();
                    redirectAttributes.addFlashAttribute("alertError", message);
                    return "redirect:/staff/materials/create";
                }
            } catch (IOException e) {
                model.addAttribute("message", e.getMessage());
                return "adminTemplate/error";

            }
        }
    }
    //update
    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(Model model,  @PathVariable("id") Integer id,@CookieValue(name = "token", defaultValue = "")String token , HttpServletRequest request, HttpServletResponse response) {
        //chuyen param trên link dung variable, chuyen form dung pathparam
        String url= SD_CLIENT.DOMAIN_APP_API+"/api/materials/getOne/"+id;
        try {
            MaterialDTO materialDTO=  APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    MaterialDTO.class,request,response
            );
            model.addAttribute("materialDTO", materialDTO);
            return "adminTemplate/pages/materials/update-material";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "adminTemplate/error";
        }

    }
    @RequestMapping(value = "/update-material", method = RequestMethod.POST)
    public String update(Model model, @Validated MaterialDTO materialDTO, BindingResult bindingResult, @CookieValue(name = "token", defaultValue = "")String token , RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response) {

        String url= SD_CLIENT.DOMAIN_APP_API+"/api/materials/update";
        if (bindingResult.hasErrors()) {

            model.addAttribute("materialDTO",materialDTO);

            return "adminTemplate/pages/materials/update-material";

        }else {
            try {
                APIHelper.makeApiCall(
                        url,
                        HttpMethod.PUT,
                        materialDTO,
                        token,
                        MaterialDTO.class,request,response

                );
                redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Update Material Success!! ");
                return "redirect:/staff/materials/showAll";
            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("alertError", "Oops Something Wrong!Update Material Fail!! ");
                return "redirect:/staff/materials/showAll";
            }
        }
    }

    //old
    public List<OrderDTO> getOrderList(String token,String url,String date, HttpServletRequest request, HttpServletResponse response) throws IOException {

        List<OrderDTO>list= new ArrayList<>();
        ParameterizedTypeReference<List<OrderDTO>> orderResponseType = new ParameterizedTypeReference<>() {};
        List<OrderDTO> orderList = APIHelper.makeApiCall(
                url,
                HttpMethod.GET,
                null,
                token,
                orderResponseType,request,response);

        //getlist in day
        for (OrderDTO order: orderList)
        {
            if(order.getOrderStatus().equalsIgnoreCase(orderStatusConfirm)&& order.getTimeHappen().contains(date))
            {
                list.add(order);
            }
        }
        return list;
    }

    public List<MaterialDetailDTO> getMaterialList(String token,List<OrderDTO>list, HttpServletRequest request, HttpServletResponse response) throws Exception {
        List<MaterialDetailDTO> materialList=new ArrayList<>();

        for (OrderDTO order:list)
        {
            ParameterizedTypeReference<List<MaterialDetailDTO>> responseType=new ParameterizedTypeReference<List<MaterialDetailDTO>>() {};
            String url="http://localhost:8080/api/materialDetails/byOrder/"+order.getId();

            List<MaterialDetailDTO> materials= APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType,request,response
            );

            Integer table=order.getTableAmount();

            //loop a new list
            for (MaterialDetailDTO material : materials)
            {
                boolean materialExist = false;
                //time with table to get a new material count
                material.setCount(material.getCount()*table);

                for (MaterialDetailDTO mate : materialList) {

                    //exit?
                    if (material.getMaterialsByMaterialId().getId()==mate.getMaterialsByMaterialId().getId()) {
                        //change unit if ext
                        mate.setCount(material.getCount() + mate.getCount());
                        materialExist = true;
                        break;
                    }
                }
                if(!materialExist)
                {
                    materialList.add(material);
                }
            }
        }
        return materialList;
    }


}
