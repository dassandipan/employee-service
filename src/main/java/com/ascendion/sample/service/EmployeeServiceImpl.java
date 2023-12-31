package com.ascendion.sample.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.stereotype.Service;

import com.ascendion.sample.dto.CreateEmployeeRequest;
import com.ascendion.sample.dto.EmployeeDto;
import com.ascendion.sample.dto.EmployeeSearchRequest;
import com.ascendion.sample.dto.UpdateEmployeeRequest;
import com.ascendion.sample.exception.EmployeeNotFoundException;
import com.ascendion.sample.model.Employee;
import com.ascendion.sample.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

	private final EmployeeRepository empRepo;

	//Create and Save Employee in Database using EmployeeRepository empRepo
	@Override
	public EmployeeDto create(CreateEmployeeRequest createEmployee) {
	}

	//Get Employee by Id from Database using EmployeeRepository empRepo
	@Override
	public EmployeeDto viewById(Long id) {
		Employee employee = findById(id);
		return mapModelToDto(employee);
	}

	//Get All Employees from Database using EmployeeRepository empRepo
	@Override
	public List<EmployeeDto> getAll() {
		return empRepo.findAll().stream().map(this::mapModelToDto).collect(Collectors.toList());
	}

	//Update Employee by Id in Database using EmployeeRepository empRepo
	@Override
	public EmployeeDto update(Long id, UpdateEmployeeRequest updateEmployee) {
		Employee employee = findById(id);
		BeanUtils.copyProperties(updateEmployee, employee);
		employee = empRepo.save(employee);
		return mapModelToDto(employee);
	}

	//Delete Employee by Id from Database using EmployeeRepository empRepo
	@Override
	public void delete(Long id) {
		Employee employee = findById(id);
		empRepo.delete(employee);
	}

	//Search Employee by Name, Designation and Department from Database using EmployeeRepository empRepo
	@Override
	public List<EmployeeDto> search(EmployeeSearchRequest request) {
		Employee employee = new Employee(request.getName(), request.getDesignation(), request.getDepartment());
		ExampleMatcher matcher = ExampleMatcher.matching().withIgnoreNullValues().withIgnoreCase()
				.withMatcher("name", GenericPropertyMatcher::contains)
				.withMatcher("designation", GenericPropertyMatcher::contains)
				.withMatcher("department", GenericPropertyMatcher::contains);
		Example<Employee> example = Example.of(employee, matcher);
		List<Employee> employees = empRepo.findAll(example);
		return employees.stream().map(this::mapModelToDto).collect(Collectors.toList());
	}

	//Find Employee by Id from Database using EmployeeRepository empRepo
	private Employee findById(Long id) {
		Optional<Employee> isEmployee = empRepo.findById(id);
		if (isEmployee.isPresent()) {
			return isEmployee.get();
		}
		log.debug("Employee not found for requested id=" + id);
		throw new EmployeeNotFoundException(id);
	}

	//Map Employee to EmployeeDto
	private EmployeeDto mapModelToDto(Employee employee) {
		EmployeeDto employeeDto = new EmployeeDto();
		BeanUtils.copyProperties(employee, employeeDto);
		return employeeDto;
	}

}
