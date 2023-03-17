package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.FoodImageDTO;
import com.springboot.wmproject.DTO.VenueImgDTO;
import com.springboot.wmproject.entities.FoodImages;
import com.springboot.wmproject.entities.VenueImages;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.FoodImgRepository;
import com.springboot.wmproject.services.FoodImgService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodImgServiceImp implements FoodImgService {

    private ModelMapper modelMapper;
    private FoodImgRepository foodImgRepository;

    public FoodImgServiceImp(ModelMapper modelMapper, FoodImgRepository foodImgRepository) {
        this.modelMapper = modelMapper;
        this.foodImgRepository = foodImgRepository;
    }

    @Override
    public List<FoodImageDTO> getAllFoodImg() {
        return foodImgRepository.findAll().stream().map(foodImages -> mapToDTO(foodImages)).collect(Collectors.toList());
    }

    @Override
    public List<FoodImageDTO> getAllFoodImgByFoodId(int id) {
        return foodImgRepository.getAllFoodImgByFoodId(id).stream().map(foodImages -> mapToDTO(foodImages)).collect(Collectors.toList());
    }

    @Override
    public FoodImageDTO getOneFoodImgById(int id) {
        return mapToDTO(foodImgRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Food Image","Id",String.valueOf(id))));
    }

    @Override
    public List<FoodImageDTO> createMultipleFoodImg(List<FoodImageDTO> foodImageDTOS) {
        foodImgRepository.saveAll(mapToEntityMultiple(foodImageDTOS));
        return foodImageDTOS;
    }

    @Override
    public FoodImageDTO updateFoodImg(FoodImageDTO foodImageDTO) {
        if(foodImageDTO!=null){
            FoodImages foodImages=foodImgRepository.findById(foodImageDTO.getId()).orElseThrow(()->new ResourceNotFoundException("Food Image","Id",String.valueOf(foodImageDTO.getId())));
            if(foodImages!=null){
                FoodImages updateFoodImg=new FoodImages();
                updateFoodImg.setId(foodImageDTO.getId());
                updateFoodImg.setUrl(foodImageDTO.getUrl());
                updateFoodImg.setFoodId(foodImageDTO.getFoodId());
                foodImgRepository.save(updateFoodImg);
                return mapToDTO(updateFoodImg);
            }
        }
        return null;
    }

    @Override
    public void deleteFoodImg(int id) {
        FoodImages foodImages=foodImgRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Food img","id",String.valueOf(id)));
        foodImgRepository.delete(foodImages);
    }
    public FoodImageDTO mapToDTO(FoodImages foodImages) {
        FoodImageDTO foodImageDTO = modelMapper.map(foodImages, FoodImageDTO.class);
        return foodImageDTO;
    }

    public FoodImages mapToEntity(FoodImageDTO foodImageDTO) {
        FoodImages foodImages = modelMapper.map(foodImageDTO, FoodImages.class);
        return foodImages;
    }

    public List<FoodImages> mapToEntityMultiple(List<FoodImageDTO> foodImageDTOS) {
        List<FoodImages> list = new ArrayList<>();
        for (FoodImageDTO item : foodImageDTOS
        ) {
            FoodImages foodImages = modelMapper.map(item, FoodImages.class);
            list.add(foodImages);
        }
        return list;
    }
}
