package org.mainlogic.spring.xa.employee;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.JmsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class EmployeeService {

	@Autowired
	private EmployeeRepository employeeRepository;

	@Autowired
	private EmployeeMessageRepository employeeMessageRepository;

	@Transactional(rollbackFor = Exception.class)
	public void addEmployee(Employee emp, boolean fail) throws JsonProcessingException {

		employeeRepository.create(emp);
		employeeMessageRepository.send(emp);

		if (fail)
			throw new IllegalStateException(".......Throwing exception explicitly......");
	}

	@Transactional
	public Employee receiveMessage() throws JmsException, IOException {

		return employeeMessageRepository.receive();
	}
}
