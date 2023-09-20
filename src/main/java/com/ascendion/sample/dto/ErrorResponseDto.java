package com.ascendion.sample.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDto implements Serializable {

	private static final long serialVersionUID = 4802305410875463159L;

	private String message;

	private List<String> details;
}
