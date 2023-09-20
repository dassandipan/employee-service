package com.ascendion.sample.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.ascendion.sample.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

	List<Employee> findByNameIgnoreCaseContainingOrDesignationIgnoreCaseContainingOrDepartmentIgnoreCaseContaining(
			String name, String designation, String department);

	List<Employee> findByNameLike(String name);
}
