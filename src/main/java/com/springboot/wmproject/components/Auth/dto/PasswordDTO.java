package com.springboot.wmproject.components.Auth.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class PasswordDTO {
    @NotEmpty
    private String newPassword;
    @NotEmpty
    private String confirmPassword;
    @NotEmpty
    private String token;
}
