package com.springboot.wmproject.components.Auth;

import com.springboot.wmproject.DTO.RefreshTokenDTO;
import com.springboot.wmproject.components.Token.TokenService;
import com.springboot.wmproject.entities.RefreshToken;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenService extends TokenService<RefreshToken, RefreshTokenDTO> {
//    RefreshTokenDTO create(RefreshToken newRefreshToken);
//    RefreshTokenDTO getOneByRefreshToken(String refreshToken);
//    RefreshTokenDTO getOneByCustomerId(int customerId);
//    RefreshTokenDTO getOneByEmployeeId(int employeeId);
//    void delete(String refreshToken);
//
//    void update(RefreshTokenDTO refreshTokenDTO);

}
