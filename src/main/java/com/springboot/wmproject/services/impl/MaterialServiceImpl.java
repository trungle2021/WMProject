package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.MaterialDTO;
import com.springboot.wmproject.DTO.OrderDTO;
import com.springboot.wmproject.DTO.VenueDTO;
import com.springboot.wmproject.entities.*;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.FoodRepository;
import com.springboot.wmproject.repositories.MaterialRepository;
import com.springboot.wmproject.repositories.OrderRepository;
import com.springboot.wmproject.services.MaterialService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.springboot.wmproject.utils.SD.orderStatusConfirm;

@Service
public class MaterialServiceImpl  implements MaterialService {
    private MaterialRepository materialRepository;
    private OrderRepository orderrepo;
    private ModelMapper modelMapper;

    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository, OrderRepository orderrepo, ModelMapper modelMapper) {
        this.materialRepository = materialRepository;
        this.orderrepo = orderrepo;
        this.modelMapper = modelMapper;
    }




    @Override
    public List<MaterialDTO> getAllMaterialByFoodId(Integer foodId) {

      return  materialRepository.getAllMaterialByFoodId(foodId).stream().map(material->mapToDTO(material)).collect(Collectors.toList());


    }

    @Override
    public List<MaterialDTO> getAllMaterial() {
        return materialRepository.findAll().stream().map(materials -> mapToDTO(materials)).collect(Collectors.toList());
    }

    @Override
    public List<MaterialDTO> getAllMaterialByOrder(Integer orderId) {
       Orders myOrder= orderrepo.findById(orderId).orElseThrow(()->new ResourceNotFoundException("Orders","id",String.valueOf(orderId)));
try {
    if (myOrder.getOrderStatus().equalsIgnoreCase(orderStatusConfirm)) {
        List<Materials> list = new ArrayList<>();
        List<Food> foodList = new ArrayList<>();
        for (FoodDetails fdt : myOrder.getFoodDetailsById()) {
            Food test=fdt.getFoodByFoodId();
            foodList.add(test);
        }

        for (Food food : foodList) {
            for (Materials foodMaterial : food.getMaterialsById()) {
                //get Id material
                String foodMaterialCode = foodMaterial.getMaterialCode();
                boolean materialExist = false;
                //loop a new list
                for (Materials material : list) {

                    //exit?
                    if (material.getMaterialCode().equalsIgnoreCase(foodMaterialCode)) {
                        //change unit if ext
                        material.setCount(material.getCount() + foodMaterial.getCount());
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
        return list.stream().map(materials -> mapToDTO(materials)).collect(Collectors.toList());
        //
    }
                }catch (Exception ex)
            {
                return null;
            }

        return null;
    }

    @Override
    public MaterialDTO getOneMaterial(int materialId) {
        Materials materials= materialRepository.findById(materialId).orElseThrow(()->new ResourceNotFoundException("material","id",String.valueOf(materialId)));

        return mapToDTO(materials);
    }

    @Override
    public MaterialDTO createMaterial(MaterialDTO newMaterialDTO) {

        Materials materials=mapToEntity(newMaterialDTO);
        Materials newMaterial=  materialRepository.save(materials);
        return mapToDTO(newMaterial);
    }

    @Override
    public MaterialDTO updateMaterial(MaterialDTO editMaterialDTO) {
        Materials checkMaterial= materialRepository.getById(editMaterialDTO.getId());
        if(checkMaterial !=null)
        {
            Materials editMaterial= mapToEntity(editMaterialDTO);

            materialRepository.save(editMaterial);
            return mapToDTO(editMaterial);
        }

        return null;
    }

    @Override
    public void deleteMaterial(int materialId) {
        Materials materials=materialRepository.findById(materialId).orElseThrow(() -> new ResourceNotFoundException("material","id",String.valueOf(materialId)));
        materialRepository.delete(materials);
    }
    public MaterialDTO mapToDTO(Materials material)
    {
        MaterialDTO materialDTO=modelMapper.map(material,MaterialDTO.class);
        return materialDTO;
    }
    public Materials mapToEntity(MaterialDTO materialDTO)
    { Materials materials=modelMapper.map(materialDTO,Materials.class);
    return materials;
    }

}
