package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.RefreshTokenDTO;
import com.springboot.wmproject.entities.RefreshToken;
import com.springboot.wmproject.exceptions.RefreshTokenNotFoundException;
import com.springboot.wmproject.repositories.RefreshTokenRepository;
import com.springboot.wmproject.services.AuthServices.RefreshTokenService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private ModelMapper modelMapper;
    private RefreshTokenRepository rftRepository;

    @Autowired
    public RefreshTokenServiceImpl(ModelMapper modelMapper, RefreshTokenRepository rftRepository) {
        this.modelMapper = modelMapper;
        this.rftRepository = rftRepository;
    }

    @Override
    public RefreshTokenDTO create(RefreshToken newRefreshToken) {
        RefreshToken refreshToken = rftRepository.save(newRefreshToken);
        return mapToDTO(refreshToken);
    }

    @Override
    public RefreshTokenDTO getOneByRefreshToken(String refreshToken) {
        RefreshToken refreshToken1 = rftRepository.getOneByRefreshToken(refreshToken).orElseThrow(()->new RefreshTokenNotFoundException("Refresh Token Not Found"));
         RefreshTokenDTO refreshTokenDTO = (mapToDTO(refreshToken1));
        if(refreshTokenDTO == null){
            throw new RefreshTokenNotFoundException("Refresh Token Not Found");
        }
        return refreshTokenDTO;
    }


    @Override
    public RefreshTokenDTO getOneByCustomerId(int customerId) {
        RefreshTokenDTO refreshTokenDTO = mapToDTO(rftRepository.getOneByCustomerId(customerId).orElseThrow(()->new RefreshTokenNotFoundException("Refresh Token Not Found")));
        if(refreshTokenDTO == null){
            throw new RefreshTokenNotFoundException("Refresh Token Not Found");
        }
        return refreshTokenDTO;
    }

    @Override
    public RefreshTokenDTO getOneByEmployeeId(int employeeId) {
        RefreshTokenDTO refreshTokenDTO = mapToDTO(rftRepository.getOneByEmployeeId(employeeId).orElseThrow(()->new RefreshTokenNotFoundException("Refresh Token Not Found")));
        if(refreshTokenDTO == null){
            throw new RefreshTokenNotFoundException("Refresh Token Not Found");
        }
        return refreshTokenDTO;
    }

    @Override
    public void delete(String refreshToken) {
        RefreshToken token = rftRepository.findByToken(refreshToken).orElseThrow(() -> new RefreshTokenNotFoundException("Refresh Token Not Found"));
        rftRepository.delete(token);
    }

    @Override
    public void update(RefreshTokenDTO refreshTokenDTO) {

    }

    public RefreshToken mapToEntity(RefreshTokenDTO refreshTokenDTO){
        RefreshToken refreshToken = modelMapper.map(refreshTokenDTO,RefreshToken.class);
        return refreshToken;
    }

    public RefreshTokenDTO mapToDTO(RefreshToken refreshToken){
        RefreshTokenDTO refreshTokenDTO = modelMapper.map(refreshToken,RefreshTokenDTO.class);
        return refreshTokenDTO;
    }
}
