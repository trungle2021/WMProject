package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.ServiceDTO;
import com.springboot.wmproject.DTO.ServiceDetailDTO;
import com.springboot.wmproject.entities.ServiceDetails;
import com.springboot.wmproject.entities.Services;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.ServiceDetailRepository;
import com.springboot.wmproject.repositories.ServiceRepository;
import com.springboot.wmproject.services.ServiceDetail_Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServiceDetail_ServiceImpl implements ServiceDetail_Service {
    private ModelMapper modelMapper;
    private ServiceDetailRepository DtRepository;
    @Autowired
    public ServiceDetail_ServiceImpl(ModelMapper modelMapper, ServiceDetailRepository dtRepository) {
        this.modelMapper = modelMapper;
        DtRepository = dtRepository;
    }







    @Override
    public List<ServiceDetailDTO> getAllDetailByOrder(Integer orderId) {
        return DtRepository.findAllByOrder(orderId).stream().map(serivceDt->mapToDTO(serivceDt)).collect(Collectors.toList());
    }

    @Override
    public List<ServiceDetailDTO> getAllDetailByService(Integer serviceId) {
        return DtRepository.findAllByService(serviceId).stream().map(serviceDetails -> mapToDTO(serviceDetails)).collect(Collectors.toList());
    }

    @Override
    public ServiceDetailDTO createDetail(ServiceDetailDTO serviceDetailDTO) {
        ServiceDetails checkDetail=DtRepository.findById(serviceDetailDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Servuce Detail","id",String.valueOf(serviceDetailDTO.getId())));
        if(checkDetail!=null)
        {
            ServiceDetails newServiceDt= mapToEntity(serviceDetailDTO);
           ServiceDetails newDt= DtRepository.save(newServiceDt);
            return mapToDTO(newDt);

        }
        return null;
    }

    @Override
    public void deleteDetail(Integer svDetailId) {
        ServiceDetails svDetail=DtRepository.findById(svDetailId).orElseThrow(() -> new ResourceNotFoundException("service","id",String.valueOf(svDetailId)));
        DtRepository.delete(svDetail);
    }


    public ServiceDetails mapToEntity(ServiceDetailDTO dto)
    {
        ServiceDetails entity=modelMapper.map(dto, ServiceDetails.class);
        return entity;

    }
    public  ServiceDetailDTO mapToDTO(ServiceDetails entity)
    {
        ServiceDetailDTO dto=modelMapper.map(entity, ServiceDetailDTO.class);
        return dto;

    }
}
