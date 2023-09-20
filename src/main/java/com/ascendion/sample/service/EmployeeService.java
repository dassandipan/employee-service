package com.ascendion.sample.service;

import java.util.List;

import com.ascendion.sample.dto.CreateEmployeeRequest;
import com.ascendion.sample.dto.EmployeeDto;
import com.ascendion.sample.dto.EmployeeSearchRequest;
import com.ascendion.sample.dto.UpdateEmployeeRequest;

public interface EmployeeService {

	EmployeeDto viewById(Long id);

	List<EmployeeDto> getAll();

	EmployeeDto create(CreateEmployeeRequest createEmployee);

	EmployeeDto update(Long id, UpdateEmployeeRequest updateEmployee);

	void delete(Long id);

	List<EmployeeDto> search(EmployeeSearchRequest request);

}
