package wm.clientmvc.controllers.Admin;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import wm.clientmvc.DTO.OrderIn3MonthDTO;
import wm.clientmvc.DTO.RevenueYearDTO;
import wm.clientmvc.utils.APIHelper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static wm.clientmvc.utils.SD_CLIENT.api_getOrderCount3Month;
import static wm.clientmvc.utils.SD_CLIENT.api_getRevenueByYear;

@Controller
@RequestMapping("/staff/revenues")
public class RevenueController {
    @GetMapping(value = "/getRevenueByYear/{year}",produces = "application/json")
    @ResponseBody
    public Map<String, Object> getRevenueByYear(@PathVariable("year") int year, @CookieValue(name = "token", defaultValue = "") String token) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        try{
            RevenueYearDTO response_api = APIHelper.makeApiCall(api_getRevenueByYear + year, HttpMethod.GET,null,token,RevenueYearDTO.class);
            response.put("result", "success");
            response.put("statusCode", 200);
            response.put("message", response_api);

        }catch (HttpClientErrorException e){
            String responseError = e.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();
            response.put("result", "error");
            response.put("statusCode", "400");
            response.put("message",message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }

    @GetMapping(value = "/getOrderCount3Month/{year}",produces = "application/json")
    @ResponseBody
    public Map<String, Object> getOrderCount3Month(@PathVariable("year") int year, @CookieValue(name = "token", defaultValue = "") String token) throws JsonProcessingException {
        Map<String, Object> response = new HashMap<>();
        try{
            OrderIn3MonthDTO response_api = APIHelper.makeApiCall(api_getOrderCount3Month + year, HttpMethod.GET,null,token,OrderIn3MonthDTO.class);
            response.put("result", "success");
            response.put("statusCode", 200);
            response.put("message", response_api);

        }catch (HttpClientErrorException e){
            String responseError = e.getResponseBodyAsString();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = mapper.readValue(responseError, Map.class);
            String message = map.get("message").toString();
            response.put("result", "error");
            response.put("statusCode", "400");
            response.put("message",message);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return response;
    }
}
