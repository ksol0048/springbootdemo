package kroryi.demo.repository;

import kroryi.demo.domain.Department;
import kroryi.demo.domain.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
List<Employee> findByDepartment(Department department);
}
