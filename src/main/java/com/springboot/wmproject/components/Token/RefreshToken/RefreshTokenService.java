package com.springboot.wmproject.components.Token.RefreshToken;

import com.springboot.wmproject.components.Token.TokenService;
import com.springboot.wmproject.entities.RefreshToken;

public interface RefreshTokenService extends TokenService<RefreshToken, RefreshToken> {
    String refreshToken(String refreshToken);


}
