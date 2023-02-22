package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.MaterialDTO;
import com.springboot.wmproject.DTO.OrderDTO;
import com.springboot.wmproject.DTO.VenueDTO;
import com.springboot.wmproject.entities.Materials;
import com.springboot.wmproject.entities.Orders;
import com.springboot.wmproject.entities.Venues;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.FoodRepository;
import com.springboot.wmproject.repositories.MaterialRepository;
import com.springboot.wmproject.services.MaterialService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MaterialServiceImpl  implements MaterialService {
    private MaterialRepository materialRepository;
    private ModelMapper modelMapper;
    @Autowired
    public MaterialServiceImpl(MaterialRepository materialRepository, ModelMapper modelMapper) {
        this.materialRepository = materialRepository;
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
