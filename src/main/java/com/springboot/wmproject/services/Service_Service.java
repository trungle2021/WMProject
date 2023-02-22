package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.ServiceDTO;
import com.springboot.wmproject.entities.Services;

import java.util.List;

public interface Service_Service {

    List<ServiceDTO> getAllService();

    ServiceDTO getOneService(int serviceId);
    ServiceDTO createService(ServiceDTO newServiceDTO);
    ServiceDTO updateService(ServiceDTO editServiceDTO);
    void deleteService(int serviceId);
}
