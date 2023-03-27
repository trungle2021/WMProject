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
    @Pattern(regexp = Regex.name_vietnamese,message = "First Name only allows letters and spaces. Do not include any numbers in your input.")
    private String first_name;
    @NotEmpty
    @Size(max = 45)
    @Pattern(regexp = Regex.name_vietnamese,message = "Last Name only allows letters and spaces. Do not include any numbers in your input.")
    private String last_name;
    @NotEmpty
    @Size(max = 100)
    @Pattern(regexp = Regex.address_simple,message = Regex.address_simple_message)
    private String address;
    @NotEmpty
    @Pattern(regexp = Regex.phone_vietnamese,message = Regex.phone_vietnamese_message)
    private String phone;
    @NotEmpty
    @Pattern(regexp = "Male|Female|Other")
    private String gender;
    @NotEmpty
    @Pattern(regexp = Regex.email,message = Regex.email_message)
    @Size(max = 30)
    private String email;
    private String avatar;
    @NotEmpty
    @Size(max = 15)
    private String username;
    private String password;
    private Integer customerId;
}
