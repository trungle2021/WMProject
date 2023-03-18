package wm.clientmvc.controllers.Admin;

import jakarta.ws.rs.PathParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import wm.clientmvc.DTO.MaterialDTO;
import wm.clientmvc.DTO.OrderDTO;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static wm.clientmvc.utils.Static_Status.orderStatusConfirm;

@Controller
@RequestMapping("/staff/materials")
public class AdminMaterialController {
@RequestMapping(value="/detail/{id}",method = RequestMethod.GET)
public String showMaterialbyOrder(Model model, @PathVariable Integer id, @CookieValue(name="token",defaultValue = "")String token)
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
                OrderDTO.class
             );
             list.add(order);


    //get material
    ParameterizedTypeReference<List<MaterialDTO>> responseType=new ParameterizedTypeReference<List<MaterialDTO>>() {};
    String url="http://localhost:8080/api/materials/byorder/"+id;

        List<MaterialDTO> materialList= APIHelper.makeApiCall(
            url,
                HttpMethod.GET,
                null,
                token,
                responseType
        );
        //getday
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now =  LocalDateTime.now();
        String today=now.format(formatter);


//        totalMaterial;
        model.addAttribute("today",today);
        model.addAttribute("orderList",list);
        model.addAttribute("materials",materialList);
        return "adminTemplate/pages/organize/material";
        } catch (IOException e) {
            model.addAttribute("message",e.getMessage());
            return "adminTemplate/error";
        }
    }

    @RequestMapping(value="/detail/searchId",method = RequestMethod.POST)
    public String materialSearchId(Model model,@PathParam("orderId")Integer orderId, @CookieValue(name="token",defaultValue = "")String token)
    {
//check team

        //
        //get order
        List<OrderDTO>list= new ArrayList<>();

//    ParameterizedTypeReference orderResponseType= new ParameterizedTypeReference() {};

        try {
            try {
                String oUrl="http://localhost:8080/api/orders/"+orderId;
                OrderDTO order=   APIHelper.makeApiCall(
                        oUrl,
                        HttpMethod.GET,
                        null,
                        token,
                        OrderDTO.class
                );
                list.add(order);
            }catch (Exception e)
            {
                model.addAttribute("message","Material Not Found!");
                return "adminTemplate/pages/organize/material-index";
            }




            //get material
            List<MaterialDTO> materialList=new ArrayList<>();
            try{
            ParameterizedTypeReference<List<MaterialDTO>> responseType=new ParameterizedTypeReference<List<MaterialDTO>>() {};
            String url="http://localhost:8080/api/materials/byorder/"+orderId;

                materialList= APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType
            );

            }catch (Exception e)
            {
                model.addAttribute("message","Material Not Found!");
                return "adminTemplate/pages/organize/material-index";
            }

            //getday
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDateTime now =  LocalDateTime.now();
            String today=now.format(formatter);


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

    @RequestMapping(value="/detail/searchDate",method = RequestMethod.POST)
    public String materialSearchId(Model model,@PathParam("date")String date, @CookieValue(name="token",defaultValue = "")String token)
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

             List<OrderDTO> list=getOrderList(token,ourl,date);

                //get material in day with count
          List<MaterialDTO> materialList=getMaterialList(token,list);
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
                List<OrderDTO>list=getOrderList(token,ourl,today);

                List<MaterialDTO> materialList=getMaterialList(token,list);

//        totalMaterial;
                model.addAttribute("today",today);
                model.addAttribute("orderList",list);
                model.addAttribute("materials",materialList);
                return "adminTemplate/pages/organize/material";
            }
           else{
                model.addAttribute("message","You not allow to Access this action!");
                return "adminTemplate/error";
            }


        } catch (IOException e) {
            model.addAttribute("message",e.getMessage());
            return "adminTemplate/error";
        }
    }

    public List<OrderDTO> getOrderList(String token,String url,String date) throws IOException {

        List<OrderDTO>list= new ArrayList<>();
        ParameterizedTypeReference<List<OrderDTO>> orderResponseType = new ParameterizedTypeReference<>() {};
        List<OrderDTO> orderList = APIHelper.makeApiCall(
                url,
                HttpMethod.GET,
                null,
                token,
                orderResponseType);

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

    public List<MaterialDTO> getMaterialList(String token,List<OrderDTO>list) throws IOException {
        List<MaterialDTO> materialList=new ArrayList<>();

        for (OrderDTO order:list)
        {
            ParameterizedTypeReference<List<MaterialDTO>> responseType=new ParameterizedTypeReference<List<MaterialDTO>>() {};
            String url="http://localhost:8080/api/materials/byorder/"+order.getId();

            List<MaterialDTO> materials= APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType
            );



            //loop a new list
            for (MaterialDTO material : materials)
            {
                boolean materialExist = false;

                for (MaterialDTO mate : materialList) {

                    //exit?
                    if (material.getMaterialCode().equalsIgnoreCase(mate.getMaterialCode())) {
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
