package com.ascendion.sample.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.ascendion.sample.dto.CreateEmployeeRequest;
import com.ascendion.sample.dto.EmployeeDto;
import com.ascendion.sample.dto.EmployeeSearchRequest;
import com.ascendion.sample.dto.UpdateEmployeeRequest;
import com.ascendion.sample.service.EmployeeService;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/employees", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
@NoArgsConstructor
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EmployeeDto create(@RequestBody @Valid CreateEmployeeRequest createEmployee) {
		log.debug("Create employee");
		return employeeService.create(createEmployee);
	}

	@GetMapping
	public List<EmployeeDto> viewAll() {
		log.debug("All Employees");
		return employeeService.getAll();
	}

	@GetMapping("/{id}")
	public EmployeeDto viewById(@PathVariable(name = "id") Long id) {
		log.debug("View employee- ID: {}", id);
		return employeeService.viewById(id);
	}

	@PutMapping("/{id}")
	public EmployeeDto update(@PathVariable(name = "id") Long id,
			@RequestBody @Valid UpdateEmployeeRequest updateEmployee) {
		log.debug("update employee- ID: {}", id);
		return employeeService.update(id, updateEmployee);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable(name = "id") Long id) {
		log.debug("Delete employee- ID: {}", id);
		employeeService.delete(id);
	}

	@GetMapping("/search")
	public List<EmployeeDto> search(@Valid EmployeeSearchRequest request) {
		log.debug("Search employee with query");
		return employeeService.search(request);
	}

}
