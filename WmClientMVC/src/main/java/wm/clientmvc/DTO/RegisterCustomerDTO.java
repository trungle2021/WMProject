package wm.clientmvc.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import wm.clientmvc.DTO.ValidateCustom.Name;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerDTO {
    private int id;

    @NotEmpty
    @Size(max = 45)
    @Name
    private String first_name;
    @NotEmpty
    @Size(max = 45)
    private String last_name;
    @NotEmpty
    @Size(max = 100)
    private String address;
    @NotEmpty
    @Pattern(regexp = "(84|0[3|5|7|8|9])+([0-9]{8})\\b",message = "Phone must be 10 numbers")
    private String phone;
    @NotEmpty
    @Pattern(regexp = "Male|Female|Other")
    private String gender;
    @NotEmpty
    @Pattern(regexp = "[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    @Size(max = 30)
    private String email;
    private String avatar;
    @NotEmpty
    @Size(max = 15)
    private String username;
    private String password;
    private Integer customerId;
}
