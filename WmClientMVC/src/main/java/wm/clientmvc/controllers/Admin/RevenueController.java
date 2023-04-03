package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import wm.clientmvc.DTO.OrderIn3MonthDTO;
import wm.clientmvc.DTO.ProfitYearDTO;
import wm.clientmvc.DTO.RevenueYearDTO;
import wm.clientmvc.utils.APIHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static wm.clientmvc.utils.SD_CLIENT.*;

@Controller
@RequestMapping("/staff/revenues")
public class RevenueController {
    @GetMapping(value = "/getRevenueByYear/{year}",produces = "application/json")
    @ResponseBody
    public Map<String, Object> getRevenueByYear(@PathVariable("year") int year, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        Map<String, Object> _response = new HashMap<>();
        try{
            RevenueYearDTO response_revenue_api = APIHelper.makeApiCall(api_getRevenueByYear + year, HttpMethod.GET,null,token,RevenueYearDTO.class,request,response);
            ProfitYearDTO response_profit_api = APIHelper.makeApiCall(api_getProfitByYear + year, HttpMethod.GET,null,token, ProfitYearDTO.class,request,response);
            System.out.println("Call API Get Revenue Chart");

            _response.put("result", "success");
            _response.put("statusCode", 200);
            _response.put("revenues", response_revenue_api);
            _response.put("profits", response_profit_api);

        }catch (HttpClientErrorException e){
            String responseError = e.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();
            _response.put("result", "error");
            _response.put("statusCode", "400");
            _response.put("message",message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return _response;
    }

    @GetMapping(value = "/getOrderCount3Month/{year}",produces = "application/json")
    @ResponseBody
    public Map<String, Object> getOrderCount3Month(@PathVariable("year") int year, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        Map<String, Object> _response = new HashMap<>();
        try{
            OrderIn3MonthDTO response_api = APIHelper.makeApiCall(api_getOrderCount3Month + year, HttpMethod.GET,null,token,OrderIn3MonthDTO.class,request,response);
            System.out.println("Call API Get Order Count Chart");

            _response.put("result", "success");
            _response.put("statusCode", 200);
            _response.put("message", response_api);

        }catch (HttpClientErrorException e){
            String responseError = e.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();
            _response.put("result", "error");
            _response.put("statusCode", "400");
            _response.put("message",message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return _response;
    }
}
