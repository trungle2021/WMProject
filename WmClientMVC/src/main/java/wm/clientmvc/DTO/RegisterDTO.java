package wm.clientmvc.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wm.clientmvc.DTO.ValidateCustom.FullName;

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
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b",message = "Phone must be 10 numbers and starts with 0 or 84")
    private String phone;

    @NotEmpty
    private String joinDate;

    @Min(0)
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
