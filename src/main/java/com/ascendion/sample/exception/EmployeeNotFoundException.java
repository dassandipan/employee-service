package com.ascendion.sample.exception;

public class EmployeeNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -6985010521444270149L;

	public EmployeeNotFoundException(Long id) {
		super("Employee not found for requested id = " + id);
	}

}
