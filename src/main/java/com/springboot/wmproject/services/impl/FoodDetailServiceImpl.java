package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.FoodDTO;
import com.springboot.wmproject.DTO.FoodDetailDTO;
import com.springboot.wmproject.entities.Food;
import com.springboot.wmproject.entities.FoodDetails;
import com.springboot.wmproject.entities.Materials;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.FoodDetailRepository;
import com.springboot.wmproject.services.FoodDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodDetailServiceImpl implements FoodDetailService {
    private ModelMapper modelMapper;
    private FoodDetailRepository foodDetailRepository;
@Autowired
    public FoodDetailServiceImpl(ModelMapper modelMapper, FoodDetailRepository foodDetailRepository) {
        this.modelMapper = modelMapper;
        this.foodDetailRepository = foodDetailRepository;
    }

    @Override
    public List<FoodDetailDTO> getAllDetailByFoodId(int foodId) {


        return  foodDetailRepository.getAllDetailByFoodId(foodId).stream().map(foodDt->mapToDTO(foodDt)).collect(Collectors.toList());

    }


    @Override
    public List<FoodDetailDTO> getAllDetailByOrderId(Integer orderId) {

        return  foodDetailRepository.getAllDetailByOrderId(orderId).stream().map(foodDt->mapToDTO(foodDt)).collect(Collectors.toList());



    }

    @Override
    public FoodDetailDTO createFoodDetail(FoodDetailDTO newFoodDetailDTO) {

        FoodDetails foodDetail=mapToEntity(newFoodDetailDTO);
        FoodDetails newFoodDetail=  foodDetailRepository.save(foodDetail);
        return mapToDTO(newFoodDetail);

    }

    @Override
    public FoodDetailDTO updateFoodDetail(FoodDetailDTO uFoodDetailDTO) {
        FoodDetails checkFoodDt= foodDetailRepository.getById(uFoodDetailDTO.getId());
        if(checkFoodDt !=null)
        {
            FoodDetails editfDetails= mapToEntity(uFoodDetailDTO);

            foodDetailRepository.save(editfDetails);
            return mapToDTO(editfDetails);
        }

        return null;
    }


    @Override
    public void deleteFoodDetail(int id) {
         FoodDetails foodDt =  foodDetailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Food Detail","id",String.valueOf(id)));
         foodDetailRepository.delete(foodDt);

    }
    public FoodDetailDTO mapToDTO(FoodDetails fooddetail){
        FoodDetailDTO fooddetailDTO = modelMapper.map(fooddetail, FoodDetailDTO.class);
        return fooddetailDTO;
    }

    public FoodDetails mapToEntity(FoodDetailDTO fooddetailDTO){
        FoodDetails foodDetail = modelMapper.map(fooddetailDTO,FoodDetails.class);
        return foodDetail;
    }
}
