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
import org.springframework.data.jpa.domain.Specification;
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
    public String findRoleByEmployeeID(int empID) {
        EmployeeDTO employeeDTO = getEmployeeById(empID);
        return findRoleByEmployeeID(empID);
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        //find all
        List<Employees> employeesList = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOList = employeesList.stream().filter(employees -> employees.is_deleted() == false).map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        Employees employees = employeeRepository.getEmployeeById(id);
        if(employees == null){
              throw new ResourceNotFoundException("Employee", "id", String.valueOf(id));
        }
        return mapToDto(employees);
    }

    @Override
    @Transactional
    public EmployeeDTO create(EmployeeDTO newEmployeeDTO) {

        return mapToDto(employeeRepository.save(mapToEntity(newEmployeeDTO)));
    }

    @Override
    @Transactional
    public EmployeeDTO update(EmployeeDTO dto) {
        int employeeId = dto.getId();
        if (employeeId == 0) {
            throw new WmAPIException(HttpStatus.BAD_REQUEST, "Employee ID is required to update");
        }
        //check if employee exist
        Employees checkEmployees = employeeRepository.getEmployeeById(employeeId);
        checkEmployees.setName(dto.getName());
        checkEmployees.setAddress(dto.getAddress());
        checkEmployees.setPhone(dto.getPhone());
        checkEmployees.setJoinDate(dto.getJoinDate());
        checkEmployees.setSalary(dto.getSalary());
        checkEmployees.setGender(dto.getGender());
        checkEmployees.setIsLeader(dto.getIsLeader());
        if(dto.getAvatar() != null){
            checkEmployees.setAvatar(dto.getAvatar());
        }
        checkEmployees.setEmail(dto.getEmail());
        checkEmployees.setTeam_id(dto.getTeam_id());
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
    @Transactional
    public void delete(int employeeId) {
        Employees employees = employeeRepository.getEmployeeById(employeeId);
        employees.setTeam_id(null);
        employees.set_deleted(true);
        employees.setIsLeader(0);
        employeeRepository.save(employees);
    }

    public EmployeeDTO mapToDto(Employees employees) {
        EmployeeDTO employeeDTO = modelMapper.map(employees, EmployeeDTO.class);
        employeeDTO.set_deleted(employees.is_deleted());
        return employeeDTO;
    }

    public Employees mapToEntity(EmployeeDTO employeeDTO) {
        Employees employees = modelMapper.map(employeeDTO, Employees.class);
        employees.set_deleted(employeeDTO.is_deleted());
        return employees;
    }
}
