package com.ascendion.sample.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.ascendion.sample.model.Employee;
import com.ascendion.sample.model.Employee_;

public class EmployeeSpecifications {

	public static Specification<Employee> withDynamicQuery(final String name, final String designation,
			final String department) {
		return (employee, query, builder) -> {
			if (name == null && designation == null && department == null) {
				throw new IllegalStateException("At least one parameter should be provided to construct complex query");
			}
			List<Predicate> predicates = new ArrayList<>();

			if (name != null) {
				predicates.add(builder.and(builder.like(employee.get(Employee_.name), name.toLowerCase())));
			}

			if (designation != null) {
				predicates.add(
						builder.and(builder.equal(employee.get(Employee_.designation), designation.toLowerCase())));
			}

			if (department != null) {
				predicates
						.add(builder.and(builder.equal(employee.get(Employee_.department), department.toLowerCase())));
			}

			Predicate[] predicatesArray = new Predicate[predicates.size()];
			return builder.and(predicates.toArray(predicatesArray));
		};
	}

	private EmployeeSpecifications() {
		// No implementation required.
	}
}
