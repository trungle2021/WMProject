package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.entities.Employees;
import com.springboot.wmproject.entities.Orders;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.EmployeeRepository;
import com.springboot.wmproject.services.EmployeeService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
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
    public List<EmployeeDTO> getAllEmployees()throws ResourceNotFoundException {
        //find all
        List<Employees> employeesList = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOList = employeesList.stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public EmployeeDTO getOneEmployeeById(int id) {
        Employees employees=employeeRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Employee","id",String.valueOf(id)));
        return mapToDto(employees);
    }

    @Override
    public EmployeeDTO createEmployee(EmployeeDTO newEmployeeDTO) {
        if(newEmployeeDTO!=null){
            //check if new employee not null
                Employees employees=mapToEntity(newEmployeeDTO);
                Employees newEmployee=employeeRepository.save(employees);
                EmployeeDTO employeeResponse=mapToDto(newEmployee);
                return employeeResponse;
        }
        return null;
    }

    @Override
    public EmployeeDTO updateEmployee(EmployeeDTO updateEmployeeDTO) throws ResourceNotFoundException{
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
    public List<EmployeeDTO> findAllByEmpType(String empType)throws ResourceNotFoundException {
        List<EmployeeDTO> employeeDTOList=employeeRepository.findAllByEmpType(empType).stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public List<EmployeeDTO> findAllByName(String name)throws ResourceNotFoundException {
        List<EmployeeDTO> employeeDTOList=employeeRepository.findAllByName(name).stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public void deleteEmployee(int employeeId)throws ResourceNotFoundException {
        Employees employees=employeeRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee","id",String.valueOf(employeeId)));
        employeeRepository.delete(employees);
    }

    public EmployeeDTO mapToDto(Employees employees) {
        EmployeeDTO postDto = modelMapper.map(employees, EmployeeDTO.class);
        return postDto;
    }

    public Employees mapToEntity(EmployeeDTO employeeDTO) {
        Employees employees = modelMapper.map(employeeDTO, Employees.class);
        return employees;
    }
}
