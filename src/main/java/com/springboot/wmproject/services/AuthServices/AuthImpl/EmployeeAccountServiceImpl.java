package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.EmployeeAccountDTO;
import com.springboot.wmproject.entities.EmployeeAccounts;
import com.springboot.wmproject.entities.Employees;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.EmployeeAccountRepository;
import com.springboot.wmproject.repositories.EmployeeRepository;
import com.springboot.wmproject.services.AuthServices.EmployeeAccountService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class EmployeeAccountServiceImpl implements EmployeeAccountService {
    private EmployeeRepository empRepo;
    private EmployeeAccountRepository empAccRepo;
    private ModelMapper modelMapper;

    @Autowired
    public EmployeeAccountServiceImpl(EmployeeRepository empRepo, EmployeeAccountRepository empAccRepo, ModelMapper modelMapper) {
        this.empRepo = empRepo;
        this.empAccRepo = empAccRepo;
        this.modelMapper = modelMapper;
    }


    @Override
    public List<EmployeeAccountDTO> getAllEmployeeAccounts() {
        List<EmployeeAccounts> employeeAccountsList = empAccRepo.findAll();
        if (employeeAccountsList.isEmpty()) {
            return null;
        }
        List<EmployeeAccountDTO> employeeAccountDTOList = employeeAccountsList.stream().map(booking -> mapToDto(booking)).collect(Collectors.toList());
        return employeeAccountDTOList;
    }

    @Override
    public EmployeeAccountDTO getEmployeeAccountByEmployeeAccountId(int id) {
        EmployeeAccounts employeeAccount = empAccRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Account", "Id", String.valueOf(id)));
        return mapToDto(employeeAccount);
    }

    @Override
    public EmployeeAccountDTO getEmployeeAccountByEmployeeId(int id) {
        EmployeeAccounts employeeAccounts = empAccRepo.getEmployeeAccountByEmployeeId(id);
        if(employeeAccounts == null){
            throw new ResourceNotFoundException("Employee Account","emp ID",String.valueOf(id));
        }
        return mapToDto(employeeAccounts);
    }

    @Override
    public List<EmployeeAccountDTO> findByName(String name) {
        List<EmployeeAccounts> employeeAccounts = empAccRepo.findByName(name);
        return employeeAccounts.stream().map(emp -> mapToDto(emp)).collect(Collectors.toList());
    }

    @Override
    public List<EmployeeAccountDTO> filterByRole(String role) {
        List<EmployeeAccounts> employeeAccounts = empAccRepo.filterByRole(role);
        return employeeAccounts.stream().map(emp -> mapToDto(emp)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeAccountDTO create(EmployeeAccountDTO employeeAccountDTO) {
        int employeeId = employeeAccountDTO.getEmployeeId();

        if (employeeId != 0) {
            //check if employee exist
            Employees employees = empRepo.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", String.valueOf(employeeId)));
            //if employee info exist -> able to create account
            if (employees != null) {
                //check if username exist
                Optional<EmployeeAccounts> employeeAccounts = empAccRepo.findByUsername(employeeAccountDTO.getUsername());
                if (employeeAccounts.isPresent()) {
                    throw new WmAPIException(HttpStatus.BAD_REQUEST, "Username already existed");
                }
                return mapToDto(empAccRepo.save(mapToEntity(employeeAccountDTO)));
            }
        }
        return null;
    }

    @Override
    @Transactional
    public EmployeeAccountDTO update(EmployeeAccountDTO employeeAccountDTO) {
        int employeeAccountId = employeeAccountDTO.getId();
        if(employeeAccountId == 0){
            throw new WmAPIException(HttpStatus.BAD_REQUEST,"EmployeeAccount ID is required to update");
        }
        //check employee account exist
        EmployeeAccounts checkEmployeeAccount = empAccRepo.findById(employeeAccountId)
                .orElseThrow(() -> new ResourceNotFoundException("Employee Account", "id", String.valueOf(employeeAccountId)));
        checkEmployeeAccount.setUsername(employeeAccountDTO.getUsername() != null ? employeeAccountDTO.getUsername() : checkEmployeeAccount.getUsername());
        checkEmployeeAccount.setPassword(employeeAccountDTO.getPassword() != null ? employeeAccountDTO.getPassword() : checkEmployeeAccount.getPassword());
        checkEmployeeAccount.setRole(employeeAccountDTO.getRole() != null ? employeeAccountDTO.getRole() : checkEmployeeAccount.getRole());

        empAccRepo.save(checkEmployeeAccount);
            return mapToDto(checkEmployeeAccount);
    }

    @Override
    @Transactional
    public void delete(int id) {
        EmployeeAccounts checkEmployeeAccount = empAccRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee Account", "id", String.valueOf(id)));
        empAccRepo.delete(checkEmployeeAccount);
    }

    @Override
    public List<EmployeeAccounts> checkUsernameExists(String username) {
        return empAccRepo.checkUsernameExists(username);
    }


    public EmployeeAccountDTO mapToDto(EmployeeAccounts employeeAccounts) {
        EmployeeAccountDTO postDto = modelMapper.map(employeeAccounts, EmployeeAccountDTO.class);
        return postDto;
    }

    public EmployeeAccounts mapToEntity(EmployeeAccountDTO employeeAccountDTO) {
        EmployeeAccounts employeeAccounts = modelMapper.map(employeeAccountDTO, EmployeeAccounts.class);
        return employeeAccounts;
    }


}
