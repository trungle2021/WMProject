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

    @NotNull
    @Size(min = 2, max = 50)
    private String name;

    @NotNull
    @Size(min = 2, max = 50)
    private String address;

    @NotEmpty(message = "Phone cannot be empty")
    @Size(min = 10,max = 11,message = "Phone must be between 10 and 11 numbers")
    private String phone;

    @NotNull
    private String joinDate;

    @NotNull
    @Min(0)
    private Double salary;

    @NotNull
    @Email
    private String email;

    @NotNull
    private Boolean isLeader;

    @NotNull
    private int team_id;

    @NotNull
    private String gender;

    private String avatar;

    //emp_account
    @NotNull
    @Size(min = 5, max = 20)
    private String username;

    @NotNull
    @Size(min = 5, max = 20)
    private String password;

//    @NotNull
    @Size(min = 3, max = 10)
    private String role;

    private Integer employeeId;

    // getters and setters
}
