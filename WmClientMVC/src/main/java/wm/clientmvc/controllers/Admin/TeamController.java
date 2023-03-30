package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

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

    @GetMapping(value = {"/getAllEmployeeByTeamId/{id}"})
    public String getAllEmployeeByTeamId(Model model, @CookieValue(name = "token", defaultValue = "") String token,@PathVariable("id") int id) throws IOException {

        ParameterizedTypeReference<List<EmployeeDTO>> responseType = new ParameterizedTypeReference<List<EmployeeDTO>>() {};
        List<EmployeeDTO> employeeList = APIHelper.makeApiCall(
                api_employees_getAllEmployeeByTeamId + id,
                HttpMethod.GET,
                null,
                token,
                responseType
        );
        int amountMember =employeeList.size();
        if(amountMember == 0){
            return "redirect:/staff/teams/getAll";
        }
        String teamName = employeeList.stream().findFirst().get().getOrganizeTeamsByTeamId().getTeamName();
        int team_id = employeeList.stream().findFirst().get().getTeam_id();
        model.addAttribute("employeeList",employeeList);
        model.addAttribute("amountMember",amountMember);
        model.addAttribute("teamName",teamName);
        model.addAttribute("team_id",team_id);
        model.addAttribute("token",token);
        return "adminTemplate/pages/teams/details";
    }


    @PostMapping("/create")
    public String createTeam(@Valid @ModelAttribute OrganizeTeamDTO teamDTO, BindingResult result, @CookieValue(name = "token", defaultValue = "") String token, Model model, RedirectAttributes attributes) throws IOException {
       String regexTeamName = "^TEAM\\s+(?!(?:administrator|admin|ADMINISTRATOR|ADMIN)\\b)[a-zA-Z]+(\\s+[^\\d\\s]+)*$";
        if(result.hasErrors()){
                attributes.addFlashAttribute("result",result);
                 return "redirect:/staff/teams/getAll";
        }
        if(!teamDTO.getTeamName().trim().matches(regexTeamName)){
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
                Integer month=thisMonth.getValue();

//get orderlist by month
                List<OrderDTO> orderListInMonth =new ArrayList<>();
                for (OrderDTO order:confirmList)
                {
                    //get month of order
                    LocalDateTime eventDay =  LocalDateTime.parse(order.getTimeHappen(),formatter);
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
                    model.addAttribute("chosenMonth",month);
                    model.addAttribute("chosenYear",thisYear);
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
                         model.addAttribute("years", years);
                            model.addAttribute("months", months);
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
        //search
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
                    return "redirect:/staff/teams/shift-manage";
                }
//get orderlist by month
                List<OrderDTO> orderListInMonth =new ArrayList<>();
                Integer month= Integer.parseInt(monthStr);
                for (OrderDTO order:haveshiftList)
                {
                    //get month of order
                    LocalDateTime eventDay =  LocalDateTime.parse(order.getTimeHappen(),formatter);

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

                model.addAttribute("chosenMonth",month);
                model.addAttribute("chosenYear",year);
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


    @RequestMapping(value="/shift-by-team",method = RequestMethod.POST)
    public String shiftByTeam(Model model,@CookieValue(name="token",defaultValue = "")String token,@RequestParam("teamId")Integer teamId,@RequestParam("choosenMonth")String monthStr,@RequestParam("choosenYear")Integer year,RedirectAttributes redirectAttributes) throws IOException {
        ParameterizedTypeReference<List<OrganizeTeamDTO>> teamsReference= new ParameterizedTypeReference<List<OrganizeTeamDTO>>() {};
        String tUrl="http://localhost:8080/api/teams/all";
        String oUrl="http://localhost:8080/api/orders/byTeam/time/"+teamId+"/"+monthStr+"/"+year;
        try {
           List<OrganizeTeamDTO>teams= APIHelper.makeApiCall(
                    tUrl,
                    HttpMethod.GET,
                    null,
                    token,
                    teamsReference
            );
            List<OrganizeTeamDTO>teamsList=teams.stream().filter(team->!team.getTeamName().equalsIgnoreCase(teamAdmin)).collect(Collectors.toList());

            ParameterizedTypeReference<List<OrderDTO>>orderReference=new ParameterizedTypeReference<List<OrderDTO>>() {};
           List<OrderDTO> orders=APIHelper.makeApiCall(
                   oUrl,
                   HttpMethod.GET,
                   null,
                   token,
                   orderReference
           );

            model.addAttribute("orders",orders);
           model.addAttribute("teams",teamsList);
           return "adminTemplate/pages/organize/shift-detail";
        } catch (HttpClientErrorException e) {

                redirectAttributes.addFlashAttribute("alertError","No upcoming shift found !");

            return "redirect:/staff/teams/shift-manage";
        }
    }

    @RequestMapping(value="shift-change-team",method=RequestMethod.POST)
    public String changeTeam(Model model,@CookieValue(name="token",defaultValue = "")String token,@RequestParam("orderId")Integer orderId,@RequestParam("teamId")Integer teamId,RedirectAttributes redirectAttributes) throws IOException {
        String url="http://localhost:8080/api/orders/update/changeTeam/"+orderId+"/"+teamId;
        try {
            APIHelper.makeApiCall(
                    url,
                    HttpMethod.PUT,
                    null,
                    token,
                    OrderDTO.class
            );

            redirectAttributes.addFlashAttribute("alertMessage", "Congratulation!The Shift was change! ");
            return "redirect:/staff/teams/shift-manage";

        } catch (HttpClientErrorException e) {
            String message = "";
            String responseError = e.getResponseBodyAsString();
            String status = String.valueOf(e.getStatusCode().value());
            if(!responseError.isEmpty()){
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> map = mapper.readValue(responseError, Map.class);
                message = map.get("message").toString();
                redirectAttributes.addFlashAttribute("alertError",message);
            }
            return "redirect:/staff/teams/shift-manage";
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
                newShift.setId(team.getTeam_id());
                newShift.setTeamsize(team.getTotal_members());
                Map<String,Integer> map= getNumberOfShift(haveShiftList,team.getTeam_id());
                Integer numberShift=map.get("total");
                Integer confirmShift=map.get("confirm");
                newShift.setNumberOfShift(numberShift);
                newShift.setNumberOfUpcomingShift(confirmShift);
                teamShifts.add(newShift);
                }
        }
            }

        return teamShifts;
    }
    private Map<String, Integer> getNumberOfShift(List<OrderDTO>listOrder, Integer teamId)
    {
        Map<String, Integer> map = new HashMap<>();
        int confirm = 0;
        int total = 0;
        for (OrderDTO order : listOrder) {
            if (order.getOrganizeTeam() == teamId && order.getOrderStatus().equalsIgnoreCase(orderStatusConfirm)) {
                confirm++;
                total++;
            } else if (order.getOrganizeTeam() == teamId && order.getOrderStatus().equalsIgnoreCase(orderStatusCompleted) ||
                    order.getOrganizeTeam() == teamId && order.getOrderStatus().equalsIgnoreCase(orderStatusUncompleted)) {
                total++;
            }

        }


        map.put("confirm", confirm);
        map.put("total", total);
        return map;
    }
}
