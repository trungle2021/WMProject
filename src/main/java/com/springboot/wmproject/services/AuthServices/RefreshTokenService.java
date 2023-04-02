package com.springboot.wmproject.services.AuthServices;

import com.springboot.wmproject.DTO.RefreshTokenDTO;
import com.springboot.wmproject.entities.RefreshToken;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenService {
    RefreshTokenDTO create(RefreshToken newRefreshToken);
    RefreshTokenDTO getOneByRefreshToken(String refreshToken);
    RefreshTokenDTO getOneByCustomerId(int customerId);
    RefreshTokenDTO getOneByEmployeeId(int employeeId);
    void delete(String refreshToken);

    void update(RefreshTokenDTO refreshTokenDTO);

}
