package com.springboot.wmproject.components.Token;


public interface TokenService<R,A> {
   R create(A credential);
   R findByToken(String token);
   void delete(String token);
   R update(A credential);
}
