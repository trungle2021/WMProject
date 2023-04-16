package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.MaterialDTO;
import com.springboot.wmproject.DTO.MaterialDetailDTO;
import com.springboot.wmproject.DTO.OrganizeTeamDTO;
import com.springboot.wmproject.entities.*;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.MaterialDetailRepository;
import com.springboot.wmproject.repositories.OrderRepository;
import com.springboot.wmproject.services.MaterialDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.springboot.wmproject.utils.SD.orderStatusConfirm;

@Service
public class MaterialDetailServiceImpl implements MaterialDetailService {

    MaterialDetailRepository detailRepository;
    ModelMapper modelMapper;
    private OrderRepository orderrepo;
    @Autowired
    public MaterialDetailServiceImpl(MaterialDetailRepository detailRepository, ModelMapper modelMapper, OrderRepository orderrepo) {
        this.detailRepository = detailRepository;
        this.modelMapper = modelMapper;
        this.orderrepo = orderrepo;

    }





    public MaterialDetailServiceImpl(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    @Override
    public List<MaterialDetailDTO> getAllDetailByFoodId(int foodId) {

        return  detailRepository.getAllDetailByFoodId(foodId).stream().map(foodDt->mapToDTO(foodDt)).collect(Collectors.toList());

    }

    @Override
    public List<MaterialDetailDTO> getAllDetailByMaterialId(Integer materialId) {
        return  detailRepository.getAllDetailByMaterialId(materialId).stream().map(materialDetail->mapToDTO(materialDetail)).collect(Collectors.toList());

    }
//chuyen sang detai;
    @Override
    public List<MaterialDetailDTO> getAllMaterialByOrder(Integer orderId) {
        Orders myOrder = orderrepo.findById(orderId).orElseThrow(() -> new ResourceNotFoundException("Orders", "id", String.valueOf(orderId)));


        if (myOrder.getOrderStatus().equalsIgnoreCase(orderStatusConfirm)) {
            List<MaterialDetail> list = new ArrayList<>();
            List<Food> foodList = new ArrayList<>();
            for (FoodDetails fdt : myOrder.getFoodDetailsById()) {
                Food test = fdt.getFoodByFoodId();
                foodList.add(test);
            }
            for (Food food : foodList) {
                for (MaterialDetail foodMaterial : food.getMaterialDetailById()) {
                    //get Id material
                    Integer foodMaterialCode = foodMaterial.getMaterialId();
                    boolean materialExist = false;
                    //loop a new list
                    for (MaterialDetail materialdt : list) {

                        //exit?
                        if (materialdt.getMaterialsByMaterialId().getId() == foodMaterialCode) {
                            //change unit if ext
                            materialdt.setCount(materialdt.getCount() + foodMaterial.getCount());
                            materialExist = true;
                            break;
                        }
                    }
                    //add new if exit
                    if (!materialExist) {
                        list.add(foodMaterial);
                    }
                }

            }
            return list.stream().map(materialDetail -> mapToDTO(materialDetail)).collect(Collectors.toList());
            //
        }
        return null;
    }





    @Override
    public MaterialDetailDTO createMaterialDetail(MaterialDetailDTO newMaterialDetailDTO) {

        MaterialDetail materialDetail=mapToEntity(newMaterialDetailDTO);
        MaterialDetail newMaterialDetail=  detailRepository.save(materialDetail);
        return mapToDTO(newMaterialDetail);


    }

    @Override
    public MaterialDetailDTO updateMaterialDetail(MaterialDetailDTO uMaterialDetail) {
        MaterialDetail checkMaterialDt= detailRepository.getById(uMaterialDetail.getId());
        if(checkMaterialDt !=null)
        {
            MaterialDetail editmDetails= mapToEntity(uMaterialDetail);

            detailRepository.save(editmDetails);
            return mapToDTO(editmDetails);
        }
        return null;
    }

    @Override
    public void deleteMaterialDetail(int id) {
        MaterialDetail materialDetail =  detailRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Material Detail","id",String.valueOf(id)));
        detailRepository.delete(materialDetail);
    }


    public MaterialDetail mapToEntity(MaterialDetailDTO materialDetailDTO) {
        MaterialDetail materialDt = modelMapper.map(materialDetailDTO, MaterialDetail.class);

        return materialDt;
    }

    public MaterialDetailDTO mapToDTO(MaterialDetail materialDt) {
        MaterialDetailDTO materialDetailDTO = modelMapper.map(materialDt, MaterialDetailDTO.class);

        return materialDetailDTO;
    }
}
