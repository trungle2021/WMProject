package wm.clientmvc.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDTO {
    @NotEmpty
    @Size(max = 15)
    private String username;
    @NotEmpty
    @Size(max = 30)
    private String password;
}
