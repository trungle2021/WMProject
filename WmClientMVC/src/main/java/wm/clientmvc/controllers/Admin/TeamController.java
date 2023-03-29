package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import wm.clientmvc.DTO.*;
import wm.clientmvc.securities.UserDetails.CustomUserDetails;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;

import static wm.clientmvc.utils.SD_CLIENT.*;
import static wm.clientmvc.utils.Static_Status.*;

@Controller
@RequestMapping("/staff/teams")
public class TeamController {
    @GetMapping(value = {"/getAll"})
    public String getAll(Model model, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, RedirectAttributes attributes) throws IOException {
        String message = (String) model.asMap().get("message");
        ParameterizedTypeReference<List<TeamSummaryDTO>> responseTypeTeam = new ParameterizedTypeReference<List<TeamSummaryDTO>>() {};
        String errorMessage = (String) model.asMap().get("errorMessage");


        try {

            List<TeamSummaryDTO> teamSummary = APIHelper.makeApiCall(
                    api_getSummaryTeamOrganization,
                    HttpMethod.GET,
                    null,
                    token,
                    responseTypeTeam
            );

            model.addAttribute("teamSummary", teamSummary);
            model.addAttribute("teams", new OrganizeTeamDTO());


            BindingResult result = (BindingResult) model.asMap().get("result");
            if(result != null || errorMessage != null){
                model.addAttribute("errorMessage",errorMessage);
                model.addAttribute("result",result);
                return "adminTemplate/pages/teams/index";

            }

        } catch (HttpClientErrorException  | HttpServerErrorException e) {
            String responseError = e.getResponseBodyAsString();
            String status = String.valueOf(e.getStatusCode().value());
            if(!responseError.isEmpty()){
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(responseError, Map.class);
                errorMessage = map.get("message").toString();
            }
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "403":
                    return "/access-denied";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                default:
                    model.addAttribute("message",message);
                    return "adminTemplate/error";
            }
        }
        model.addAttribute("message",message);

        return "adminTemplate/pages/teams/index";
    }



    @PostMapping("/create")
    public String createTeam(@Valid @ModelAttribute OrganizeTeamDTO teamDTO, BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, Model model, RedirectAttributes attributes) throws IOException {
       String regexTeamName = "^TEAM\\s+(?!(?:administrator|admin|ADMINISTRATOR|ADMIN)\\b)[a-zA-Z]+(\\s+[^\\d\\s]+)*$";
        if(result.hasErrors()){
                attributes.addFlashAttribute("result",result);
                 return "redirect:/staff/teams/getAll";
        }
        if(!teamDTO.getTeamName().matches(regexTeamName)){
            model.addAttribute("errorMessage","Input must start with 'TEAM ' and have at least one letter after it and do not contain 'admin or administrator' word.");
            return "redirect:/staff/teams/getAll";
        }
        try {
         OrganizeTeamDTO organizeTeamDTO =   APIHelper.makeApiCall(
                    api_teams_create,
                    HttpMethod.POST,
                    teamDTO,
                    token,
                    OrganizeTeamDTO.class
            );

        }catch (HttpClientErrorException  | HttpServerErrorException e) {
            String message = "";
            String responseError = e.getResponseBodyAsString();
            String status = String.valueOf(e.getStatusCode().value());
            if(!responseError.isEmpty()){
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(responseError, Map.class);
                message = map.get("message").toString();
            }
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "403":
                    return "/access-denied";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                default:
                    model.addAttribute("message",message);
                    return "adminTemplate/error";
            }
        }
        attributes.addFlashAttribute("message","Create Team Success");
        return "redirect:/staff/teams/getAll";
    }

    @PostMapping("/update")
    public String updateTeam(@Valid @ModelAttribute OrganizeTeamDTO teamDTO, BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, Model model, RedirectAttributes attributes) throws IOException {
        if(result.hasErrors()){
            String errorMessage = result.getFieldError().getDefaultMessage();
            attributes.addFlashAttribute("errorMessage",errorMessage);
            return "redirect:/staff/teams/getAll";
        }
        try {
            APIHelper.makeApiCall(
                    api_teams_update,
                    HttpMethod.PUT,
                    teamDTO,
                    token,
                    OrganizeTeamDTO.class
            );
        }catch (HttpClientErrorException  | HttpServerErrorException e) {
            String message = "";
            String responseError = e.getResponseBodyAsString();
            String status = String.valueOf(e.getStatusCode().value());
            if(!responseError.isEmpty()){
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(responseError, Map.class);
                message = map.get("message").toString();
            }
            switch (status) {
                case "401":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/staff/login";
                case "403":
                    return "/access-denied";
                case "404":
                    attributes.addFlashAttribute("errorMessage", message);
                    return "redirect:/404-not-found";
                default:
                    model.addAttribute("message",message);
                    return "adminTemplate/error";
            }
        }
        attributes.addFlashAttribute("message","Update Team Success");
        return "redirect:/staff/teams/getAll";
    }


    @PostMapping(value = "/delete/{id}",produces = "application/json")
    @ResponseBody
    public Map<String, Object> deleteTeam(@PathVariable int id ,@CookieValue(name = "token", defaultValue = "") String token, Model model, RedirectAttributes attributes) throws IOException {

        Map<String, Object> response = new HashMap<>();

        try{
            String response_api = APIHelper.makeApiCall(api_teams_delete + id,HttpMethod.DELETE,null,token,String.class);
            response.put("result", "success");
            response.put("statusCode", 200);
            response.put("message", response_api);

        }catch (HttpClientErrorException e){
            String responseError = e.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();
            response.put("result", "success");
            response.put("statusCode", e.getStatusCode());
            response.put("message",message);
        }

        return response;
    }


    //khang

    //organize team
    @RequestMapping("/showallorder/organize")

    public String showAllbyOrganize(Model model, @CookieValue(name = "token",defaultValue = "")String token,@ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError)
    {
        List<Integer> years = Arrays.asList(2023, 2024,2025,2026,2027,2028,2029,2030);
        List<Month> months = Arrays.asList(
                Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL,
                Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST,
                Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER
        );
        model.addAttribute("years", years);
        model.addAttribute("months", months);


        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails empUserDetails= (CustomUserDetails) authentication.getPrincipal();
        Long empId= empUserDetails.getUserId();

        ParameterizedTypeReference<List<OrderDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        //confirm only
        String url = "http://localhost:8080/api/orders/byTeam/empId/"+empId;
        try {
            List<OrderDTO> orderList = APIHelper.makeApiCall(url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType);


            model.addAttribute("list",orderList);

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

            return "adminTemplate/pages/organize/order-organize-manage";


        } catch (HttpClientErrorException | IOException ex) {
            model.addAttribute("message", ex.getMessage());
            return "adminTemplate/error";
        }

    }



    @RequestMapping(value = "/showallorder/organize/search",method = RequestMethod.POST)

    public String searchOrderbyDay(Model model, @CookieValue(name = "token",defaultValue = "")String token, @RequestParam("month") String monthStr, @RequestParam("year") Integer year)
    {
        List<Integer> years = Arrays.asList(2023,2024,2025,2026,2027,2028,2029,2030);
        List<Month> months = Arrays.asList(
                Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL,
                Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST,
                Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER
        );
        model.addAttribute("years", years);
        model.addAttribute("months", months);
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails empUserDetails= (CustomUserDetails) authentication.getPrincipal();
        Long empId= empUserDetails.getUserId();

        ParameterizedTypeReference<List<OrderDTO>> responseType = new ParameterizedTypeReference<>() {
        };
        //confirm only
        String url = "http://localhost:8080/api/orders/byTeam/empId/"+empId;
        try {
            List<OrderDTO> orderList = APIHelper.makeApiCall(url,
                    HttpMethod.GET,
                    null,
                    token,
                    responseType);

            List<OrderDTO> orderListInMonth =new ArrayList<>();
            for (OrderDTO order:orderList)
            {
            //get month of order
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime eventDay =  LocalDateTime.parse(order.getTimeHappen(),formatter);
                Integer month= Integer.parseInt(monthStr);
                Month happenMonth=eventDay.getMonth();
                int happenYear=eventDay.getYear();

                if(happenMonth.getValue()==month && happenYear==year)
                {
                    orderListInMonth.add(order);
                }
            }

            model.addAttribute("list",orderListInMonth);

            return "adminTemplate/pages/organize/order-organize-manage";


        } catch (HttpClientErrorException | IOException ex) {
            model.addAttribute("message", ex.getMessage());
            return "adminTemplate/error";
        }

    }

    @RequestMapping(value="/shift-manage")
    public String TeamMangeShift(Model model, @CookieValue(name = "token",defaultValue = "")String token,@ModelAttribute("alertMessage") String alertMessage,@ModelAttribute("alertError") String alertError)
    {
        List<Integer> years = Arrays.asList(2023,2024,2025,2026,2027,2028,2029,2030);
        List<Month> months = Arrays.asList(
                Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL,
                Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST,
                Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER
        );
        model.addAttribute("years", years);
        model.addAttribute("months", months);


        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String role=authentication.getAuthorities().stream().findFirst().toString();
        if(role.contains("ADMIN")) {
            String url="http://localhost:8080/api/orders/have-shift-order";
            String tUrl="http://localhost:8080/api/teams/getSummaryTeamOrganization";
            ParameterizedTypeReference<List<OrderDTO>> typeReference=new ParameterizedTypeReference<List<OrderDTO>>() {};
            ParameterizedTypeReference<List<TeamSummaryDTO>> teamReference=new ParameterizedTypeReference<List<TeamSummaryDTO>>() {};
            try {
                List<OrderDTO> confirmList= APIHelper.makeApiCall(
                         url,
                         HttpMethod.GET,
                         null,
                         token,
                         typeReference
                 );
//ngày
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime now=LocalDateTime.now();

                Month thisMonth=now.getMonth();
                Integer thisYear =now.getYear();

//get orderlist by month
                List<OrderDTO> orderListInMonth =new ArrayList<>();
                for (OrderDTO order:confirmList)
                {
                    //get month of order
                    LocalDateTime eventDay =  LocalDateTime.parse(order.getTimeHappen(),formatter);
                    Integer month=thisMonth.getValue();
                    Month happenMonth=eventDay.getMonth();
                    int happenYear=eventDay.getYear();

                    if(happenMonth.getValue()==month && happenYear==thisYear)
                    {
                        orderListInMonth.add(order);
                    }
                }
                //get team summary
                List<TeamSummaryDTO> teamList= APIHelper.makeApiCall(
                        tUrl,
                        HttpMethod.GET,
                        null,
                        token,
                        teamReference
                );
                    List<TeamShift>list=getShiftList(teamList,orderListInMonth);

                    model.addAttribute("list",list);


//get alert
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

                    return "adminTemplate/pages/organize/shift-manage";

            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "adminTemplate/error";
            }

        }else {
            //confirm only
            model.addAttribute("message", "You are not allow to use this action!");
            return "adminTemplate/error";

        }
    }

    @RequestMapping(value="/shift-manage/search")
    public String TeamMangeShiftSearch(Model model, @CookieValue(name = "token",defaultValue = "")String token,@RequestParam("month") String monthStr, @RequestParam("year") Integer year,RedirectAttributes redirectAttributes)
    {
        List<Integer> years = Arrays.asList(2023, 2024,2025,2026,2027,2028,2029,2030);
        List<Month> months = Arrays.asList(
                Month.JANUARY, Month.FEBRUARY, Month.MARCH, Month.APRIL,
                Month.MAY, Month.JUNE, Month.JULY, Month.AUGUST,
                Month.SEPTEMBER, Month.OCTOBER, Month.NOVEMBER, Month.DECEMBER
        );
        model.addAttribute("years", years);
        model.addAttribute("months", months);


        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String role=authentication.getAuthorities().stream().findFirst().toString();
        if(role.contains("ADMIN")) {
            String url="http://localhost:8080/api/orders/have-shift-order";
            String tUrl="http://localhost:8080/api/teams/getSummaryTeamOrganization";
            ParameterizedTypeReference<List<OrderDTO>> typeReference=new ParameterizedTypeReference<List<OrderDTO>>() {};
            ParameterizedTypeReference<List<TeamSummaryDTO>> teamReference=new ParameterizedTypeReference<List<TeamSummaryDTO>>() {};
            try {
                List<OrderDTO> haveshiftList= APIHelper.makeApiCall(
                        url,
                        HttpMethod.GET,
                        null,
                        token,
                        typeReference
                );
//ngày
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

                if(monthStr.isEmpty() || year==null)
                {
                    redirectAttributes.addFlashAttribute("alertError", "Oops Something wrong!month and year is empty!");
                    return "redirect:/staff/teams/shift-manage/";
                }
//get orderlist by month
                List<OrderDTO> orderListInMonth =new ArrayList<>();
                for (OrderDTO order:haveshiftList)
                {
                    //get month of order
                    LocalDateTime eventDay =  LocalDateTime.parse(order.getTimeHappen(),formatter);
                    Integer month= Integer.parseInt(monthStr);
                    Month happenMonth=eventDay.getMonth();
                    int happenYear=eventDay.getYear();

                    if(happenMonth.getValue()==month && happenYear==year)
                    {
                        orderListInMonth.add(order);
                    }
                }
                //get team summary
                List<TeamSummaryDTO> teamList= APIHelper.makeApiCall(
                        tUrl,
                        HttpMethod.GET,
                        null,
                        token,
                        teamReference
                );
                List<TeamShift>list=getShiftList(teamList,orderListInMonth);

                model.addAttribute("list",list);
                return "adminTemplate/pages/organize/shift-manage";

            } catch (Exception e) {
                model.addAttribute("message", e.getMessage());
                return "adminTemplate/error";
            }


        }else {
            //confirm only
            model.addAttribute("message", "You are not allow to use this action!");
            return "adminTemplate/error";

        }



    }



    private List<TeamShift> getShiftList(List<TeamSummaryDTO> teams,List<OrderDTO> haveShiftList)
    {

        List<TeamShift> teamShifts=new ArrayList<>();
        if(teams!=null){
        for (TeamSummaryDTO team:teams)
        {
                if(!team.getTeam_name().equalsIgnoreCase(teamAdmin)){
                TeamShift newShift = new TeamShift();
                newShift.setTeamName(team.getTeam_name());
                newShift.setTeamLeader(team.getLeader_name());
                newShift.setTeamsize(team.getTotal_members());
                Integer numberShift= getNumberOfShift(haveShiftList,team.getTeam_id());
                newShift.setNumberOfShift(numberShift);
                teamShifts.add(newShift);
                }
        }
            }

        return teamShifts;
    }
    private Integer getNumberOfShift(List<OrderDTO>listOrder,Integer teamId)
    {
        int count=0;
        for (OrderDTO order:listOrder)
        {
         if(order.getOrganizeTeam()==teamId && order.getOrderStatus().equalsIgnoreCase(orderStatusConfirm)||
            order.getOrganizeTeam()==teamId && order.getOrderStatus().equalsIgnoreCase(orderStatusCompleted)||
            order.getOrganizeTeam()==teamId && order.getOrderStatus().equalsIgnoreCase(orderStatusUncompleted))
         {
             count++;

         }
        }
        return count;
    }




}
