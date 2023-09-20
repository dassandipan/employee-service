package com.ascendion.sample.dto;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UpdateEmployeeRequest extends CreateEmployeeRequest {

	
	private static final String PHONE_REGEX = "^[0-9]*$";
	
	@Size(min=10, max=13, message = "phone should have minimun 10 characters and maximum 13 characters length")
	@Pattern(regexp = PHONE_REGEX, message = "Invalid phone number")	
	private String phone;

	@Size(max = 255, message = "address should have maximum 255 characters length")
	private String address;

}
