package com.ascendion.sample.controller;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.ascendion.sample.dto.CreateEmployeeRequest;
import com.ascendion.sample.model.Employee;
import com.ascendion.sample.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class EmployeeControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private EmployeeRepository mockRepository;

	@BeforeEach
	public void init() {
		Employee employee = createSampleEmployee();
		when(mockRepository.findById(1001L)).thenReturn(Optional.of(employee));
	}

	@Test
	void find_employeeId_OK() throws Exception {
		// @formatter:off
		mockMvc.perform(
				get("/employees/{id}", 1001)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1001)))
				.andExpect(jsonPath("$.name", is("employee name")))
				.andExpect(jsonPath("$.designation", is("employee designation")))
				.andExpect(jsonPath("$.department", is("employee department")))
				.andExpect(jsonPath("$.phone", is("9000011111")))
				.andExpect(jsonPath("$.address", is("employee address")));
		// @formatter:on
		verify(mockRepository, times(1)).findById(1001L);
	}

	@Test
	void find_employeeIdNotFound_404() throws Exception {
		// @formatter:off
		mockMvc.perform(get("/employees/{id}", 1005)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
		// @formatter:on
	}

	@Test
	void find_allEmployee_OK() throws Exception {
		Employee employee1 = createSampleEmployee();
		Employee employee2 = createEmployee(1002L, "employee name two", "employee designation two",
				"employee department two", "9000011112", "employee address2");
		List<Employee> employees = Arrays.asList(employee1, employee2);

		when(mockRepository.findAll()).thenReturn(employees);
		// @formatter:off
		mockMvc.perform(get("/employees")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id", is(1001)))
				.andExpect(jsonPath("$[0].name", is("employee name")))
				.andExpect(jsonPath("$[0].designation", is("employee designation")))
				.andExpect(jsonPath("$[0].department", is("employee department")))
				.andExpect(jsonPath("$[0].phone", is("9000011111")))
				.andExpect(jsonPath("$[0].address", is("employee address")))
				.andExpect(jsonPath("$[1].id", is(1002)))
				.andExpect(jsonPath("$[1].name", is("employee name two")))
				.andExpect(jsonPath("$[1].designation", is("employee designation two")))
				.andExpect(jsonPath("$[1].department", is("employee department two")))
				.andExpect(jsonPath("$[1].phone", is("9000011112")))
				.andExpect(jsonPath("$[1].address", is("employee address2")));
		// @formatter:on
		verify(mockRepository, times(1)).findAll();
	}

	@Test
	void save_employee_OK() throws Exception {
		Employee employee = createSampleEmployee();
		CreateEmployeeRequest createEmployee = new CreateEmployeeRequest();
		createEmployee.setName("employee name");
		createEmployee.setDesignation("employee designation");
		createEmployee.setDepartment("employee department");
		when(mockRepository.saveAndFlush(any(Employee.class))).thenReturn(employee);
		// @formatter:off
		mockMvc.perform(post("/employees")
				.content(objectMapper.writeValueAsString(createEmployee))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", is(1001)))
				.andExpect(jsonPath("$.name", is("employee name")))
				.andExpect(jsonPath("$.designation", is("employee designation")))
				.andExpect(jsonPath("$.department", is("employee department")));
		// @formatter:on
		verify(mockRepository, times(1)).saveAndFlush(any(Employee.class));
	}

	@Test
	void save_emptyName_nullDesignation_400() throws Exception {
		Employee employee = createEmployee(null, "", null, "department", null, null);
		// @formatter:off
		mockMvc.perform(post("/employees")
				.content(objectMapper.writeValueAsString(employee))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message", is("Validation Failed")))
				.andExpect(jsonPath("$.details").isArray())
				.andExpect(jsonPath("$.details", hasSize(4)))
				.andExpect(jsonPath("$.details", hasItem("name should not be empty or null")))
				.andExpect(jsonPath("$.details", hasItem("name should have minimun 2 characters and maximum 50 characters length")))
				.andExpect(jsonPath("$.details", hasItem("Invalid name - no special characters allowed")))
				.andExpect(jsonPath("$.details", hasItem("designation should not be empty or null")));
		// @formatter:on
		verify(mockRepository, times(0)).saveAndFlush(any(Employee.class));
	}

	@Test
	void update_employee_OK() throws Exception {
		Employee updateEmployee = updatePyload();
		Employee employee = createSampleEmployee();
		when(mockRepository.save(any(Employee.class))).thenReturn(employee);
		// @formatter:off
		mockMvc.perform(put("/employees/{id}", 1001)
				.content(objectMapper.writeValueAsString(updateEmployee))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1001)))
				.andExpect(jsonPath("$.name", is("employee name")))
				.andExpect(jsonPath("$.designation", is("employee designation")))
				.andExpect(jsonPath("$.department", is("employee department")))
				.andExpect(jsonPath("$.phone", is("9000011111")))
				.andExpect(jsonPath("$.address", is("employee address")));
		// @formatter:on
	}

	@Test
	void delete_employee_OK() throws Exception {
		doNothing().when(mockRepository).delete(any(Employee.class));
		// @formatter:off
		mockMvc.perform(delete("/employees/{id}", 1001)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(status().isOk());
		// @formatter:on
		verify(mockRepository, times(1)).delete(createSampleEmployee());
	}

	
	@Test
	void search_employee_OK() throws Exception {
		Employee employee = createSampleEmployee();
		List<Employee> employees = Arrays.asList(employee);
		when(mockRepository.findAll(any(Example.class))).thenReturn(employees);
		// @formatter:off
		mockMvc.perform(get("/employees/search")
				.param("name", "employee")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON))
				// .andDo(print())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(jsonPath("$[0].id", is(1001)))
				.andExpect(jsonPath("$[0].name", is("employee name")))
				.andExpect(jsonPath("$[0].designation", is("employee designation")))
				.andExpect(jsonPath("$[0].department", is("employee department")))
				.andExpect(jsonPath("$[0].phone", is("9000011111")))
				.andExpect(jsonPath("$[0].address", is("employee address")));
		// @formatter:on
		verify(mockRepository, times(1)).findAll(any(Example.class));
	}

	private Employee createSampleEmployee() {
		return createEmployee(1001L, "employee name", "employee designation", "employee department", "9000011111",
				"employee address");
	}

	private Employee updatePyload() {
		return createEmployee(null, "employee name", "employee designation", "employee department", "9000011111",
				"employee address");
	}

	private Employee createEmployee(Long id, String name, String designation, String department, String phone,
			String address) {
		Employee employee = new Employee(name, designation, department);
		if (id != null) {
			employee.setId(id);
		}
		if (phone != null) {
			employee.setPhone(phone);
		}
		if (address != null) {
			employee.setAddress(address);
		}
		return employee;
	}
}
