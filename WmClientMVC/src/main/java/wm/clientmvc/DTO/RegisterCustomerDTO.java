package wm.clientmvc.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterCustomerDTO {
    private int id;
    private String name;
    private String address;
    private String phone;
    private String gender;
    private String avatar;
    private String pin;
    private Integer customerId;
}
