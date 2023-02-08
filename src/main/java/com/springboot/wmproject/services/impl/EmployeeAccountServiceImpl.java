package com.springboot.wmproject.services.impl;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.entities.Employees;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.repositories.EmployeeAccountRepository;
import com.springboot.wmproject.repositories.EmployeeRepository;
import com.springboot.wmproject.services.EmployeeAccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class EmployeeAccountServiceImpl implements EmployeeAccountService {
    private EmployeeRepository employeesRepository;
    private EmployeeAccountRepository employeeAccountRepository;
    private ModelMapper modelMapper;

    public EmployeeAccountServiceImpl(EmployeeRepository employeesRepository,EmployeeAccountRepository employeeAccountRepository, ModelMapper modelMapper) {
        this.employeesRepository=employeesRepository;
        this.employeeAccountRepository = employeeAccountRepository;
        this.modelMapper = modelMapper;
    }

    @Autowired

    @Override
    public List<EmployeeAccountDTO> getAllEmployeeAccounts() {
        List<EmployeeAccounts> employeeAccountsList=employeeAccountRepository.findAll();
        List<EmployeeAccountDTO> employeeAccountDTOList =  employeeAccountsList.stream().map(booking -> mapToDto(booking)).collect(Collectors.toList());
        return employeeAccountDTOList;
    }

    @Override
    public EmployeeAccountDTO getOneEmployeeAccount(int employeeAccountId) {
        EmployeeAccounts employeeAccounts=employeeAccountRepository.findById(employeeAccountId).orElseThrow(()->new ResourceNotFoundException("EmployeeAccount","id",String.valueOf(employeeAccountId)));
        if(employeeAccounts!=null){
            EmployeeAccountDTO employeeAccountDTO=mapToDto(employeeAccounts);
            return employeeAccountDTO;
        }
        return null;
    }

    @Override
    public EmployeeAccountDTO createEmployeeAccount(EmployeeAccountDTO employeeAccountDTO) {
        int employeeId=employeeAccountDTO.getEmployeeId();
        //check employee exist
        if(employeeId!=0){
            Employees checkEmployees=employeesRepository.findById(employeeId).orElseThrow(()->new ResourceNotFoundException("Employee","id",String.valueOf(employeeId)));
            //if exist create account for employee
            if (checkEmployees!=null){
                EmployeeAccounts employeeAccount=mapToEntity(employeeAccountDTO);
                EmployeeAccounts newEmployeeAccount=employeeAccountRepository.save(employeeAccount);
                EmployeeAccountDTO employeeAccountResponse=mapToDto(newEmployeeAccount);
                return employeeAccountResponse;
            }
        }
        return null;
    }

    @Override
    public EmployeeAccountDTO updateEmployeeAccount(EmployeeAccountDTO employeeAccountDTO) {
        int employeeAccountId=employeeAccountDTO.getEmployeeId();
        EmployeeAccounts checkEmployeeAccount=employeeAccountRepository.findById(employeeAccountId).orElseThrow(()->new ResourceNotFoundException("Employee Account","id",String.valueOf(employeeAccountId)));
        if(checkEmployeeAccount!=null){
            EmployeeAccounts updateEmployeeAccount=new EmployeeAccounts();
            updateEmployeeAccount.setId(employeeAccountDTO.getId());
            updateEmployeeAccount.setUsername(employeeAccountDTO.getUsername());
            updateEmployeeAccount.setPassword(employeeAccountDTO.getPassword());
            updateEmployeeAccount.setRole(employeeAccountDTO.getRole());
            updateEmployeeAccount.setEmployeeId(checkEmployeeAccount.getEmployeeId());
            employeeAccountRepository.save(updateEmployeeAccount);
            return mapToDto(updateEmployeeAccount);
        }
        return null;
    }

    @Override
    public void deleteEmployeeAccount(int employeeAccountId) {
        EmployeeAccounts checkEmployeeAccount=employeeAccountRepository.findById(employeeAccountId).orElseThrow(()->new ResourceNotFoundException("Employee Account","id",String.valueOf(employeeAccountId)));
        employeeAccountRepository.delete(checkEmployeeAccount);
    }
    public EmployeeAccountDTO mapToDto(EmployeeAccounts employeeAccounts){
        EmployeeAccountDTO postDto = modelMapper.map(employeeAccounts, EmployeeAccountDTO.class);
        return postDto;
    }

    public EmployeeAccounts mapToEntity(EmployeeAccountDTO employeeAccountDTO){
        EmployeeAccounts employeeAccounts = modelMapper.map(employeeAccountDTO, EmployeeAccounts.class);
        return employeeAccounts;
    }
}
