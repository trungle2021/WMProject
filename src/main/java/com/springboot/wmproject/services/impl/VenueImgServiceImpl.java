package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.OrderDTO;
import com.springboot.wmproject.DTO.VenueDTO;
import com.springboot.wmproject.DTO.VenueImgDTO;
import com.springboot.wmproject.entities.Orders;
import com.springboot.wmproject.entities.VenueImages;
import com.springboot.wmproject.entities.Venues;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.VenueImgRepository;
import com.springboot.wmproject.services.VenueImgService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class VenueImgServiceImpl implements VenueImgService {

    private VenueImgRepository venueImgRepository;
    private ModelMapper modelMapper;

    public VenueImgServiceImpl(VenueImgRepository venueImgRepository, ModelMapper modelMapper) {
        this.venueImgRepository = venueImgRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<VenueImgDTO> getAllVenueImg() {
        return venueImgRepository.findAll().stream().map(venueImages -> mapToDTO(venueImages)).collect(Collectors.toList());
    }

    @Override
    public List<VenueImgDTO> getSomeVenueImg() {
        return venueImgRepository.findAll().stream().limit(4).map(venueImages -> mapToDTO(venueImages)).collect(Collectors.toList());
    }

    @Override
    public List<VenueImgDTO> getAllVenueImgByVenueId(int id) throws ResourceNotFoundException {

        return venueImgRepository.getAllById(id).stream().map(venueImages -> mapToDTO(venueImages)).collect(Collectors.toList());
    }

    @Override
    public VenueImgDTO createVenueImg(VenueImgDTO venueImgDTO) {
        if (venueImgDTO != null) {
            venueImgRepository.save(mapToEntity(venueImgDTO));
        }
        return venueImgDTO;
    }

    @Override
    public List<VenueImgDTO> createMultipleVenueImg(List<VenueImgDTO> venueImgDTO) {
        venueImgRepository.saveAll(mapToEntityMultiple(venueImgDTO));
        return venueImgDTO;
    }

    @Override
    public VenueImgDTO updateVenueImg(VenueImgDTO venueImgDTO) {
        if (venueImgDTO != null) {
            VenueImages venueImages = venueImgRepository.findById(venueImgDTO.getId()).orElseThrow(() -> new ResourceNotFoundException("Venues image", "Id", String.valueOf(venueImgDTO.getId())));
            venueImages.setUrl(venueImgDTO.getUrl());
            venueImages.setVenueId(venueImgDTO.getVenueId());
            venueImgRepository.save(venueImages);
            return mapToDTO(venueImages);
        }
        return null;
    }

    @Override
    public void deleteVenueImg(int id) {
        VenueImages venueImages = venueImgRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Venues image", "Id", String.valueOf(id)));
        venueImgRepository.delete(venueImages);
    }

    public VenueImgDTO mapToDTO(VenueImages venueImages) {
        VenueImgDTO venueImgDTO = modelMapper.map(venueImages, VenueImgDTO.class);
        return venueImgDTO;
    }

    public VenueImages mapToEntity(VenueImgDTO venueImgDTO) {
        VenueImages venueImages = modelMapper.map(venueImgDTO, VenueImages.class);
        return venueImages;
    }

    public List<VenueImages> mapToEntityMultiple(List<VenueImgDTO> venueImgDTO) {
        List<VenueImages> list = new ArrayList<>();
        for (VenueImgDTO item : venueImgDTO
        ) {
            VenueImages venueImages = modelMapper.map(item, VenueImages.class);
            list.add(venueImages);
        }
        return list;
    }
}
