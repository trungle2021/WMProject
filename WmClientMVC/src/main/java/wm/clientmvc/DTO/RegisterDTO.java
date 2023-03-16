package wm.clientmvc.DTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDTO {
    private int id;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String name;

    @NotEmpty
    @Size(min = 2, max = 50)
    private String address;

    @NotEmpty(message = "Phone cannot be empty")
    @Size(min = 10,max = 11,message = "Phone must be between 10 and 11 numbers")
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
