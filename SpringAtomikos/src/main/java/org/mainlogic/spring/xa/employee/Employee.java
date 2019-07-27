package org.mainlogic.spring.xa.employee;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(exclude = { "id" })
@JsonPropertyOrder({ "name", "id" })
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
}
