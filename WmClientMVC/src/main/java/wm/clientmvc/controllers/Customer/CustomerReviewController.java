package wm.clientmvc.controllers.Customer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import wm.clientmvc.DTO.CustomerAccountDTO;
import wm.clientmvc.DTO.CustomerDTO;
import wm.clientmvc.DTO.ReviewDTO;
import wm.clientmvc.utils.APIHelper;
import wm.clientmvc.utils.SD_CLIENT;

import java.io.IOException;
import java.rmi.server.ExportException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/customer/review")
public class CustomerReviewController {
    @GetMapping("/{cusId}")
    public String index(HttpServletRequest request, Model model, @PathVariable("cusId") String id) {
        model.addAttribute("cusId", id);
        return "customerTemplate/blog-review";
    }

    @PostMapping("/create")
    public String createReview(Model model, @RequestParam("cusId") String cusId, @ModelAttribute ReviewDTO reviewDTO, @CookieValue(name = "token", defaultValue = "") String token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        LocalDateTime currentDate = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String finalDate = currentDate.format(formatter);
        reviewDTO.setDatePost(String.valueOf(finalDate));
        String[] badWords = {"damn", "hell", "crap", "sucks", "freaking"};
        String checkWords = reviewDTO.getContent().toLowerCase();
        int count = 0;
        for (String item : badWords) {
            if (checkWords.contains(item)) {
                count++;
            }
        }
        if (count == 0 && reviewDTO.getRating() > 3) {
            reviewDTO.setActive(true);
        } else {
            reviewDTO.setActive(false);
        }
        try {
            CustomerAccountDTO accountDTO = APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/customerAccounts/customer/" + cusId,
                    HttpMethod.GET,
                    null,
                    token,
                    CustomerAccountDTO.class
            );
            reviewDTO.setCustomerAccountId(accountDTO.getId());
            APIHelper.makeApiCall(
                    SD_CLIENT.DOMAIN_APP_API + "/api/reviews/create",
                    HttpMethod.POST,
                    reviewDTO,
                    token,
                    String.class
            );
            model.addAttribute("alertMessage", "Thank you for your review");
        } catch (HttpClientErrorException ex) {
            throw new ExportException(ex.getMessage());
        }

        return "redirect:/home";
    }
}
