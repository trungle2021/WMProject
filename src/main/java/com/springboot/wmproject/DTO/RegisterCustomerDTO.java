package com.springboot.wmproject.DTO;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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

    @NotEmpty
    @Size(max = 45)
    private String firstname;
    @NotEmpty
    @Size(max = 45)
    private String lastname;
    @NotEmpty
    @Size(max = 100)
    private String address;
    @NotEmpty
    @Pattern(regexp = "^(01[23689]|09)[0-9]{8}$")
    private String phone;
    @NotEmpty
    @Pattern(regexp = "Male|Female|Other")
    private String gender;
    @NotEmpty
    @NotEmpty
    @Pattern(regexp = "[a-z0-9._-]+@[a-z0-9.-]+\\.[a-z]{2,}$")
    @Size(max = 30)
    private String email;
    private String avatar;
    @NotEmpty
    @Size(max = 15)
    private String username;
    @NotEmpty
    @Size(max = 30)
    private String password;
    private Integer customerId;
}
