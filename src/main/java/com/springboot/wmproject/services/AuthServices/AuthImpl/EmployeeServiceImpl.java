package com.springboot.wmproject.services.AuthServices.AuthImpl;

import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.entities.Employees;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.EmployeeRepository;
import com.springboot.wmproject.services.AuthServices.EmployeeService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public List<EmployeeDTO> getAllEmployees() {
        //find all
        List<Employees> employeesList = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOList = employeesList.stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        Employees employees = employeeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", String.valueOf(id)));
        return mapToDto(employees);
    }

    @Override
    @Transactional
    public EmployeeDTO create(EmployeeDTO newEmployeeDTO) {

        return mapToDto(employeeRepository.save(mapToEntity(newEmployeeDTO)));
    }

    @Override
    public EmployeeDTO update(EmployeeDTO updateEmployeeDTO) {
        int employeeId = updateEmployeeDTO.getId();
        if (employeeId == 0) {
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "Employee ID is required to update");
        }
        //check if employee exist
        Employees checkEmployees = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", String.valueOf(employeeId)));
        checkEmployees.setName(updateEmployeeDTO.getName());
        checkEmployees.setAddress(updateEmployeeDTO.getAddress());
        checkEmployees.setPhone(updateEmployeeDTO.getPhone());
        checkEmployees.setJoinDate(updateEmployeeDTO.getJoinDate());
        checkEmployees.setSalary(updateEmployeeDTO.getSalary());
        checkEmployees.setGender(updateEmployeeDTO.getGender());
        checkEmployees.setIsLeader(updateEmployeeDTO.getIsLeader());
        if(updateEmployeeDTO.getAvatar() != null){
            checkEmployees.setAvatar(updateEmployeeDTO.getAvatar());
        }
        checkEmployees.setEmail(updateEmployeeDTO.getEmail());
        checkEmployees.setTeam_id(updateEmployeeDTO.getTeam_id());
        employeeRepository.save(checkEmployees);
        return mapToDto(checkEmployees);

    }

    @Override
    public List<EmployeeDTO> findAllByRole(String role) {
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllByRole(role).stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public List<Employees> checkPhoneExists(String phone) {
        List<Employees> employeesList = employeeRepository.checkPhoneExists(phone);
        return employeesList;
    }

    @Override
    public List<Employees> checkEmailExists(String email) {
        return employeeRepository.checkEmailExists(email);
    }

    @Override
    public List<EmployeeDTO> findAllByName(String name) {
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllByName(name).stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public List<EmployeeDTO> findAllByTeamId(Integer teamId) {
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllTeamId(teamId).stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public void delete(int employeeId) {
        Employees employees = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException("Employee", "id", String.valueOf(employeeId)));
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
