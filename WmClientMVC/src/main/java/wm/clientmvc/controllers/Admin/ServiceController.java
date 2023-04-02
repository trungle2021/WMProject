package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.FoodDTO;
import wm.clientmvc.DTO.ServiceDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/staff/services")

public class ServiceController {





    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String create(Model model) {
        //tao đối tượng cho view
        model.addAttribute("serviceDTO", new ServiceDTO());

        return "adminTemplate/pages/Services/add_service";
    }

    @RequestMapping(value = "/create-service", method = RequestMethod.POST)
    public String create(Model model,@Validated @ModelAttribute ServiceDTO serviceDTO, BindingResult bindingResult, @CookieValue(name = "token", defaultValue = "") String token, RedirectAttributes redirectAttributes) {



        if(serviceDTO.getCost()>=serviceDTO.getPrice())
        {
            bindingResult.rejectValue("cost","cost.range","cost must smaller than price!" );

        }

        if (bindingResult.hasErrors()) {

            model.addAttribute("serviceDTO",serviceDTO);

            return "adminTemplate/pages/Services/add_service";

        }else {
            try {
                ServiceDTO service = APIHelper.makeApiCall(
                        "http://localhost:8080/api/services/create",
                        HttpMethod.POST,
                        serviceDTO,
                        token,
                        ServiceDTO.class
                );

                if (service != null) {
                    redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Create Service Success!! ");
                    return "redirect:/staff/services/showAll";
                } else {
                    redirectAttributes.addFlashAttribute("alertError", "Oops Something Wrong!Create Service Fail!! ");
                    return "redirect:/staff/services/showAll";

                }
            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "adminTemplate/error";

            }
        }
    }

    @RequestMapping("/showAll")
    public String showAllService (Model model, @CookieValue(name = "token", defaultValue = "") String token, @ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError)
    {
        ParameterizedTypeReference<List<ServiceDTO>> typeReference=new ParameterizedTypeReference<List<ServiceDTO>>() {};
        String url=SD_CLIENT.DOMAIN_APP_API+"/api/services/allactive";
        try {
            List<ServiceDTO> list =APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    typeReference

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


            return "adminTemplate/pages/Services/show_service";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "adminTemplate/error";
        }
    }

    @RequestMapping(value = "/update/{id}", method = RequestMethod.GET)
    public String update(Model model,  @PathVariable("id") Integer id,@CookieValue(name = "token", defaultValue = "")String token ) {
        //chuyen param trên link dung variable, chuyen form dung pathparam
        String url= SD_CLIENT.DOMAIN_APP_API+"/api/services/getOne/"+id;
        try {
          ServiceDTO service=  APIHelper.makeApiCall(
                    url,
                    HttpMethod.GET,
                    null,
                    token,
                    ServiceDTO.class

            );
            model.addAttribute("serviceDTO", service);
            return "adminTemplate/pages/Services/detail_service";
        } catch (Exception e) {
            model.addAttribute("message", e.getMessage());
            return "adminTemplate/error";
        }

    }
    @RequestMapping(value = "/update-service", method = RequestMethod.POST)
    public String update(Model model, @Validated ServiceDTO serviceDTO, BindingResult bindingResult, @CookieValue(name = "token", defaultValue = "")String token , RedirectAttributes redirectAttributes) {

        String url= SD_CLIENT.DOMAIN_APP_API+"/api/services/update";
        serviceDTO.setActive(true);
        if(serviceDTO.getCost()>=serviceDTO.getPrice())
        {
            bindingResult.rejectValue("cost","cost.range","cost must smaller than price!" );

        }
        if (bindingResult.hasErrors()) {

            model.addAttribute("serviceDTO",serviceDTO);

            return "adminTemplate/pages/Services/detail_service";

        }else {
            try {
                APIHelper.makeApiCall(
                        url,
                        HttpMethod.PUT,
                        serviceDTO,
                        token,
                        ServiceDTO.class

                );
                redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Update Service Success!! ");
                return "redirect:/staff/services/showAll";


            } catch (Exception e) {
                redirectAttributes.addFlashAttribute("alertError", "Oops Something Wrong!Update Service Fail!! ");
                return "redirect:/staff/services/showAll";
            }
        }
    }
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.GET)
    public String update(Model model,@PathVariable("id") int id,@CookieValue(name = "token", defaultValue = "")String token ,RedirectAttributes redirectAttributes) {

        String url= SD_CLIENT.DOMAIN_APP_API+"/api/services/soft_delete/"+id;

        try {
             APIHelper.makeApiCall(
                    url,
                    HttpMethod.PUT,
                    null,
                    token,
                    ServiceDTO.class

            );
             redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!Delete Service Success!! ");
                return "redirect:/staff/services/showAll";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("alertError", "Oops Something Wrong!Delete Service Fail!! ");
            return "redirect:/staff/services/showAll";
        }

    }


}
