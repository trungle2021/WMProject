package wm.clientmvc.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wm.clientmvc.utils.Regex;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerDTO {
    private int id;

    @NotEmpty
    @Size(max = 45)
    @Pattern(regexp = Regex.name_vietnamese,message = "This field only allows letters and spaces. Please do not include any numbers in your input.")
    private String first_name;
    @NotEmpty
    @Size(max = 45)
    @Pattern(regexp = Regex.name_vietnamese,message = "This field only allows letters and spaces. Please do not include any numbers in your input.")
    private String last_name;
    @NotEmpty
    @Size(max = 100)
    private String address;
    @NotEmpty
    @Pattern(regexp = Regex.phone_vietnamese,message = "Sorry, the phone number you entered does not match the expected format. Please enter a valid phone number where the first digit starts with 84 or 0 and is followed by 3, 5, 7, 8, or 9.")
    private String phone;
    @NotEmpty
    @Pattern(regexp = "Male|Female|Other")
    private String gender;
    @NotEmpty
    @Pattern(regexp = Regex.email,message = "Please enter a valid email address in the format yourname@example.com. The email address should contain an '@' symbol and a domain name such as 'example.com'. ")
    @Size(max = 30)
    private String email;
    private String avatar;
    @NotEmpty
    @Size(max = 15)
    private String username;
    private String password;
    private Integer customerId;
}
