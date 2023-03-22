package wm.clientmvc.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wm.clientmvc.DTO.ValidateCustom.FullName;
import wm.clientmvc.utils.Regex;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private int id;

    @NotEmpty
    @FullName
    @Size(min = 2, max = 50)
    private String name;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String address;

    @NotEmpty(message = "Phone cannot be empty")
    @Pattern(regexp = Regex.phone_vietnamese,message = "Sorry, the phone number you entered does not match the expected format. Please enter a valid phone number where the first digit starts with 84 or 0 and is followed by 3, 5, 7, 8, or 9.")
    private String phone;

    @NotEmpty
    private String joinDate;

    @Min( value = 0, message = "Value must be greater than or equal to 0")
    @Max(value = 1000000, message = "Value must be less than or equal to 1,000,000$")
    private Double salary;

    @NotEmpty
    @Email
    private String email;

    private Integer isLeader;

    private Integer team_id;

    @NotEmpty
    private String gender;

    private String avatar;

    //emp_account
    @NotEmpty
    @Size(min = 5, max = 20)
    private String username;

    private String password;

//    @NotNull
    @Size(min = 3, max = 10)
    private String role;

    private Integer employeeId;

    // getters and setters
}
