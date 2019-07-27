package org.mainlogic.spring.xa;

import java.io.IOException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mainlogic.spring.xa.employee.Employee;
import org.mainlogic.spring.xa.employee.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jms.JmsException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringAtomikosApplicationTests {

	@Autowired
	private EmployeeService employeeService;
	
	@Test
	public void createNewEmployee() throws JmsException, IOException {

		Employee emp = new Employee();
		emp.setId(2);
		emp.setName("test");

		employeeService.addEmployee(emp, false);
		System.out.println(employeeService.receiveMessage());
	}
}
