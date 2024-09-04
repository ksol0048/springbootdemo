package kroryi.demo.service;

import kroryi.demo.domain.Department;
import kroryi.demo.domain.Employee;
import kroryi.demo.repository.DepartmentRepository;
import kroryi.demo.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private DepartmentRepository departmentRepository;

    public List<Employee> getEmployeesByDepartment(String departmentName) {
        Department department = departmentRepository.findDepartmentByDepartName(departmentName);
        return employeeRepository.findByDepartment(department);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id).orElseThrow();
    }

}
