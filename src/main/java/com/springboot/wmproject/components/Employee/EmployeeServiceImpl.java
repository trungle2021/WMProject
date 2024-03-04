package com.springboot.wmproject.components.Employee;

import com.springboot.wmproject.DTO.EmployeeDTO;
import com.springboot.wmproject.entities.Employees;
import com.springboot.wmproject.exceptions.ResourceNotFoundException;
import com.springboot.wmproject.exceptions.WmAPIException;
import com.springboot.wmproject.repositories.EmployeeRepository;
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
    public String findRoleByEmployeeID(int empID) {
        EmployeeDTO employeeDTO = getEmployeeById(empID);
        return findRoleByEmployeeID(empID);
    }

    @Override
    public List<EmployeeDTO> getAllEmployeesExceptAdmin() {
        //find all
        List<Employees> employeesList = employeeRepository.findAllExceptAdmin();
        List<EmployeeDTO> employeeDTOList = employeesList.stream()
                .map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public List<EmployeeDTO> getAllEmployees() {
        //find all
        List<Employees> employeesList = employeeRepository.findAll();
        List<EmployeeDTO> employeeDTOList = employeesList.stream()
                .filter(employees -> employees.is_deleted() == false)
                .map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public EmployeeDTO getEmployeeById(int id) {
        Employees employees = employeeRepository.getEmployeeById(id);
        if (employees == null) {
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
        int isLeader = dto.getIsLeader() == null ? 0 : dto.getIsLeader();


        //check if employee exist
        Employees checkEmployees = employeeRepository.getEmployeeById(employeeId);
        checkEmployees.setName(dto.getName() != null ? dto.getName() : checkEmployees.getName());
        checkEmployees.setAddress(dto.getAddress() != null ? dto.getAddress() : checkEmployees.getAddress());
        checkEmployees.setPhone(dto.getPhone() != null ? dto.getPhone() : checkEmployees.getPhone());
        checkEmployees.setJoinDate(dto.getJoinDate() != null ? dto.getJoinDate() : checkEmployees.getJoinDate());
        checkEmployees.setSalary(dto.getSalary() != null ? dto.getSalary() : checkEmployees.getSalary());
        checkEmployees.setGender(dto.getGender() != null ? dto.getGender() : checkEmployees.getGender());

        checkEmployees.setAvatar(dto.getAvatar() != null ? dto.getAvatar() : checkEmployees.getAvatar());

        if (isLeader != checkEmployees.getIsLeader()) {
            if (isLeader == 1) {
                List<EmployeeDTO> allEmpInTeam = findAllByTeamId(checkEmployees.getTeam_id());
                if (allEmpInTeam != null) {
                    Boolean hasLeaderInTeam = allEmpInTeam.stream().filter(emp -> emp.getIsLeader() == 1).findFirst().isPresent();
                    if (!hasLeaderInTeam) {
                        checkEmployees.setIsLeader(1);
                    } else {
                        throw new WmAPIException(HttpStatus.BAD_REQUEST, checkEmployees.getOrganizeTeamsByTeamId().getTeamName() + " already has a leader");
                    }
                } else {
                    checkEmployees.setIsLeader(1);
                }
            } else {
                checkEmployees.setIsLeader(0);
            }
        } else {
            checkEmployees.setIsLeader(isLeader);
        }


        checkEmployees.setTeam_id(dto.getTeam_id() != null ? dto.getTeam_id() : checkEmployees.getTeam_id());

        checkEmployees.setEmail(dto.getEmail() != null ? dto.getEmail() : checkEmployees.getEmail());
        employeeRepository.save(checkEmployees);
        return mapToDto(checkEmployees);

    }

    @Override
    public List<EmployeeDTO> findAllByRole(String role) {
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllByRole(role).stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public boolean checkPhoneExists(String phone) {
       return employeeRepository.checkPhoneExists(phone).isEmpty();

    }

    @Override
    public boolean checkEmailExists(String email) {
        return employeeRepository.checkEmailExists(email).isEmpty();
    }

    @Override
    public List<EmployeeDTO> findAllByName(String name) {
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllByName(name).stream().map(employees -> mapToDto(employees)).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    public List<EmployeeDTO> findAllByTeamId(Integer teamId) {
        List<EmployeeDTO> employeeDTOList = employeeRepository.findAllTeamId(teamId).stream().map(this::mapToDto).collect(Collectors.toList());
        return employeeDTOList;
    }

    @Override
    @Transactional
    public void softDelete(int employeeId) {
        Employees employees = employeeRepository.getEmployeeById(employeeId);
        employees.setTeam_id(null);
        employees.set_deleted(true);
        employees.setIsLeader(0);
        employeeRepository.save(employees);
    }

    @Override
    public List<EmployeeDTO> getAllEmployeeByTeamId(Integer empId) {
        List<EmployeeDTO> employeeDTOList = employeeRepository.getAllEmployeeByTeamId(empId).stream().map(emp -> mapToDto(emp)).collect(Collectors.toList());
        return employeeDTOList;
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
