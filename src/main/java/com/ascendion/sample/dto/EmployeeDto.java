package com.ascendion.sample.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class EmployeeDto extends UpdateEmployeeRequest {

	private Long id;
}
