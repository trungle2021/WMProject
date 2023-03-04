package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.entities.Employees;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.EmployeeRepository;
import com.springboot.wmproject.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    private EmployeeRepository employeeRepository;
    private ModelMapper modelMapper;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees(){
        //find all
        List<Employees> employeesList = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOList = employeesList.stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        Employees employees=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee","id",String.valueOf(id)));
        return mapToDto(employees);
    }
    @Override
    public EmployeeDTO create(EmployeeDTO newEmployeeDTO) {
        return mapToDto(employeeRepository.save(mapToEntity(newEmployeeDTO)));
    }

    @Override
    public EmployeeDTO update(EmployeeDTO updateEmployeeDTO) {
        int employeeId = updateEmployeeDTO.getId();
        if(employeeId!=0){
            //check if employee exist
            Employees checkEmployees=employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee","id",String.valueOf(employeeId)));
            //if employee = null create new
            if(checkEmployees!=null){
                Employees employees=new Employees();
                employees.setId(updateEmployeeDTO.getId());
                employees.setName(updateEmployeeDTO.getName());
                employees.setAddress(updateEmployeeDTO.getAddress());
                employees.setPhone(updateEmployeeDTO.getPhone());
                employees.setJoinDate(updateEmployeeDTO.getJoinDate());
                employees.setSalary(updateEmployeeDTO.getSalary());
                employees.setEmpType(updateEmployeeDTO.getEmpType());
                employees.setTeamId(updateEmployeeDTO.getTeam_id());
                employeeRepository.save(employees);
                return mapToDto(employees);
            }
        }
        return null;
    }

    @Override
    public List<EmployeeDTO> findAllByEmpType(String empType){
        List<EmployeeDTO> employeeDTOList=employeeRepository.findAllByEmpType(empType).stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public List<EmployeeDTO> findAllByName(String name) {
        List<EmployeeDTO> employeeDTOList=employeeRepository.findAllByName(name).stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public void delete(int employeeId){
        Employees employees=employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee","id",String.valueOf(employeeId)));
        employeeRepository.delete(employees);
    }

//    @Override
//    public EmployeeDTO save(EmployeeDTO employeeDTO) {
//        return mapToDto(employeeRepository.save(mapToEntity(employeeDTO)));
//    }

    public EmployeeDTO mapToDto(Employees employees) {
        EmployeeDTO postDto = modelMapper.map(employees, EmployeeDTO.class);
        return postDto;
    }

    public Employees mapToEntity(EmployeeDTO employeeDTO) {
        Employees employees = modelMapper.map(employeeDTO, Employees.class);
        return employees;
    }
}
