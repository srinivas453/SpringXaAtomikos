package org.mainlogic.spring.xa.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	public void create(Employee emp) {

		jdbcTemplate.execute("insert into CUSTOMERS (id, name) values (" + emp.getId() + ", '" + emp.getName() + "')");
	}
}
