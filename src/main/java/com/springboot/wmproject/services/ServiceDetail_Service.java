package com.springboot.wmproject.services;

import com.springboot.wmproject.DTO.ServiceDetailDTO;
import com.springboot.wmproject.entities.ServiceDetails;
import com.springboot.wmproject.repositories.ServiceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ServiceDetail_Service {
    List<ServiceDetailDTO> getAllDetailByOrder(Integer orderId);
    List<ServiceDetailDTO> getAllDetailByService(Integer serviceId);

    ServiceDetailDTO createDetail(ServiceDetailDTO serviceDetailDTO);
    void deleteDetail (Integer svDetailId);

}
