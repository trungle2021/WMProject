package com.springboot.wmproject.components.Token.RefreshToken;

import com.springboot.wmproject.DTO.RefreshTokenDTO;
import com.springboot.wmproject.components.Token.TokenService;
import com.springboot.wmproject.entities.RefreshToken;
import org.springframework.data.jpa.repository.Query;

public interface RefreshTokenService extends TokenService<RefreshToken, RefreshTokenDTO> {
    String refreshToken(String refreshToken);

}
