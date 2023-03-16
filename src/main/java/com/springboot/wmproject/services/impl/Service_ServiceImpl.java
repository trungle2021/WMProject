package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.ServiceDTO;
import com.springboot.wmproject.entities.Materials;
import com.springboot.wmproject.entities.Services;
import com.springboot.wmproject.entities.Venues;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.ServiceRepository;
import com.springboot.wmproject.services.Service_Service;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class Service_ServiceImpl implements Service_Service {
    private ModelMapper modelMapper;
    private ServiceRepository SRepository;
    @Autowired
    public Service_ServiceImpl(ModelMapper modelMapper, ServiceRepository SRepository) {
        this.modelMapper = modelMapper;
        this.SRepository = SRepository;
    }

    @Override
    public List<ServiceDTO> getAllService() {
        return  SRepository.findAll().stream().map(services -> maptoDTO(services)).collect(Collectors.toList());
    }

    @Override
    public List<ServiceDTO> getAllServiceActive() {
        List<ServiceDTO>services= SRepository.findAll().stream().map(sv -> maptoDTO(sv)).collect(Collectors.toList());
        List<ServiceDTO> newList=new ArrayList<>();
        for (ServiceDTO sv:services)
        {
            if(sv.isActive())
            {
                newList.add(sv);
            }
        }
        return newList;
    }

    @Override
    public ServiceDTO getOneService(int serviceId) {
     Services services=  SRepository.findById(serviceId).orElseThrow(() -> new ResourceNotFoundException("service","id",String.valueOf(serviceId)));

       return maptoDTO(services);
    }

    @Override
    public ServiceDTO createService(ServiceDTO newServiceDTO) {
        String serviceName =  newServiceDTO.getServiceName();
        if (serviceName != null) {
            List<Services> checkService = SRepository.validServiceByName(serviceName);
            if(checkService.size() == 0){
                Services newService = SRepository.save(maptoEntity(newServiceDTO));
                return maptoDTO(newService);
            }
        }
        return null;
    }

    @Override
    public ServiceDTO updateService(ServiceDTO editServiceDTO) {
        Services checkService= SRepository.getById(editServiceDTO.getId());
        if(checkService !=null)
        {
            Services editService= maptoEntity(editServiceDTO);

            SRepository.save(editService);
            return maptoDTO(editService);
        }

        return null;
    }

    @Override
    public void deleteService(int serviceId) {
        Services services=SRepository.findById(serviceId).orElseThrow(() -> new ResourceNotFoundException("service","id",String.valueOf(serviceId)));
        SRepository.delete(services);
    }

    public Services maptoEntity(ServiceDTO dto)
    {
        Services service=modelMapper.map(dto,Services.class);
        service.setActive(dto.isActive());
        return service;
    }
    public ServiceDTO maptoDTO(Services services)
    {
        ServiceDTO dto= modelMapper.map(services,ServiceDTO.class);
        dto.setActive(services.isActive());
        return dto;
    }
}
